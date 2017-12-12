// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.marklogic.runtime;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.FailedRequestException;
import com.marklogic.client.ForbiddenUserException;
import com.marklogic.client.MarkLogicServerException;
import com.marklogic.client.ResourceNotFoundException;
import com.marklogic.client.document.DocumentDescriptor;
import com.marklogic.client.document.DocumentManager;
import com.marklogic.client.document.DocumentUriTemplate;
import com.marklogic.client.io.BytesHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.io.marker.AbstractWriteHandle;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.WriterWithFeedback;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.exceptions.MarkLogicErrorCode;
import org.talend.components.marklogic.exceptions.MarkLogicException;
import org.talend.components.marklogic.tmarklogicoutput.MarkLogicOutputProperties;
import org.talend.daikon.i18n.GlobalI18N;
import org.talend.daikon.i18n.I18nMessages;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MarkLogicWriter implements WriterWithFeedback<Result, IndexedRecord, IndexedRecord> {

    private transient static final Logger LOGGER = LoggerFactory.getLogger(MarkLogicWriter.class);
    protected static final I18nMessages MESSAGES = GlobalI18N.getI18nMessageProvider().getI18nMessages(MarkLogicWriter.class);

    private MarkLogicOutputProperties properties;

    private MarkLogicWriteOperation writeOperation;

    RuntimeContainer container;

    private DatabaseClient connectionClient;

    DocumentManager docMgr = null;

    private String docIdSuffix;

    private Result result;

    private List<IndexedRecord> successWrites;

    private List<IndexedRecord> rejectWrites;

    @Override
    public void open(String uId) throws IOException {
        this.result = new Result(uId);

        connectionClient = writeOperation.getSink().connect(container);

        initializeDocManager();
    }

    private void initializeDocManager() {
        if (connectionClient == null) {
            return;
        }
        switch (properties.docType.getValue()) {
        case MIXED:
            docMgr = connectionClient.newDocumentManager();
            docIdSuffix = "";
            break;
        case XML:
            docMgr = connectionClient.newXMLDocumentManager();
            docIdSuffix = "xml";
            break;
        case JSON:
            docMgr = connectionClient.newJSONDocumentManager();
            docIdSuffix = "json";
            break;
        case PLAIN_TEXT:
            docMgr = connectionClient.newTextDocumentManager();
            docIdSuffix = "txt";
            break;
        case BINARY:
            docMgr = connectionClient.newBinaryDocumentManager();
            docIdSuffix = "bin";
            break;
        }
    }

    @Override
    public void write(Object indexedRecordDatum) throws IOException {
        if (indexedRecordDatum == null || !(indexedRecordDatum instanceof IndexedRecord)) {
            return;
        }
        IndexedRecord indexedRecord = (IndexedRecord) indexedRecordDatum;

        String docId = (String) indexedRecord.get(0);
        Object docContent = indexedRecord.get(1);

        try {
            switch (properties.action.getValue()) {
            case DELETE:
                deleteRecord(docId);
                break;
            case PATCH:
                patchRecord(docId, (String) docContent);
                break;
            case UPSERT:
                upsertRecord(docId, docContent);
                break;
            }
            handleSuccessRecord(indexedRecord);
        }
        catch (Exception e){
            handleRejectRecord(indexedRecord, e);
        }

        result.totalCount++;
    }

    private void upsertRecord(String docId, Object docContent) {
        AbstractWriteHandle genericHandle = null;
        if (MarkLogicOutputProperties.DocType.BINARY == properties.docType.getValue()) {
            if (docContent instanceof byte[]) {
                genericHandle = new BytesHandle((byte[]) docContent);
            } else if (docContent instanceof File) {
                genericHandle = new FileHandle((File) docContent);
            } else if (docContent instanceof String) {
                genericHandle = new StringHandle((String) docContent);
            } else {
                throw new MarkLogicException(new MarkLogicErrorCode("Unsupported Content Represent in " + docContent.getClass()));
            }
        } else {
            if (docContent instanceof String) {
                genericHandle = new StringHandle((String) docContent);
            }
        }

        if (properties.autoGenerateDocId.getValue()) {
            DocumentUriTemplate template = docMgr.newDocumentUriTemplate(docIdSuffix);

            if (StringUtils.isNotEmpty(properties.docIdPrefix.getStringValue()) && !"\"\""
                    .equals(properties.docIdPrefix.getStringValue())) {
                String realPrefix = properties.docIdPrefix.getStringValue();
                if (!(realPrefix.endsWith("/") || realPrefix.endsWith("\\"))) {
                    realPrefix = realPrefix + "/";
                }
                template.setDirectory(realPrefix.replaceAll("\\\\", "/"));
            }
            DocumentDescriptor docDesc = docMgr.create(template, genericHandle);
            docId = docDesc.getUri();
        }

        docMgr.write(docId, genericHandle);
    }

    private void deleteRecord(String docId) {
        docMgr.delete(docId);
    }

    private void patchRecord(String docId, String docContent) {
        StringHandle patchHandle = new StringHandle(docContent);
        if (MarkLogicOutputProperties.DocType.JSON == properties.docType.getValue()) {
            patchHandle.withFormat(Format.JSON);
        } else if (MarkLogicOutputProperties.DocType.XML == properties.docType.getValue()) {
            patchHandle.withFormat(Format.XML);
        } else {
            throw new MarkLogicException(new MarkLogicErrorCode("Cant patch for docType " + properties.docType.getValue()));
        }

        docMgr.patch(docId, patchHandle);
    }

    private void handleSuccessRecord(IndexedRecord record) {
        result.successCount++;
        successWrites.add(record);
    }

    private void handleRejectRecord(IndexedRecord record, Exception e) {
        result.rejectCount++;
        Schema rejectSchema = properties.schemaReject.schema.getValue();
        IndexedRecord errorIndexedRecord = new GenericData.Record(rejectSchema);
        errorIndexedRecord.put(0, record.get(0) + " " + e.getMessage());

        rejectWrites.add(errorIndexedRecord);
    }

    @Override
    public Result close() throws IOException {
        if (!properties.connection.isReferencedConnectionUsed()) {
            connectionClient.release();
            LOGGER.info(MESSAGES.getMessage("info.connectionClosed"));
        }
        return result;
    }

    @Override
    public WriteOperation<Result> getWriteOperation() {
        return writeOperation;
    }

    @Override
    public Iterable<IndexedRecord> getSuccessfulWrites() {
        return successWrites;
    }

    @Override
    public Iterable<IndexedRecord> getRejectedWrites() {
        return rejectWrites;
    }

    public MarkLogicWriter(MarkLogicWriteOperation writeOperation, RuntimeContainer container,
            MarkLogicOutputProperties properties) {
        this.writeOperation = writeOperation;
        this.container = container;
        this.properties = properties;

        successWrites = new ArrayList<>();
        rejectWrites = new ArrayList<>();
    }
}
