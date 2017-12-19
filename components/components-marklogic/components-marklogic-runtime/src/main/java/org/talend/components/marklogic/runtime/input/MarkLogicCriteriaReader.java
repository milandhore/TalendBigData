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
package org.talend.components.marklogic.runtime.input;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.admin.QueryOptionsManager;
import com.marklogic.client.document.DocumentManager;
import com.marklogic.client.io.Format;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;
import org.talend.components.api.component.runtime.AbstractBoundedReader;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.exceptions.MarkLogicErrorCode;
import org.talend.components.marklogic.exceptions.MarkLogicException;
import org.talend.components.marklogic.runtime.input.strategies.DocContentReader;
import org.talend.components.marklogic.tmarklogicinput.MarkLogicInputProperties;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public class MarkLogicCriteriaReader extends AbstractBoundedReader<IndexedRecord> {

    private RuntimeContainer container;

    private MarkLogicInputProperties inputProperties;

    private DatabaseClient connectionClient;

    private IndexedRecord current;

    private Result result;

    private Schema.Field docContentField;

    private DocumentManager docManager;

    private SearchHandle searchHandle;

    private String criteria;

    private long matchedDocuments;

    private int maxRetrieve;

    private long pageSize;

    private long documentCounter;

    private int pageCounter;

    private MatchDocumentSummary[] currentPage;

    DocContentReader docContentReader;

    private QueryManager queryManager;

    private StringQueryDefinition stringQueryDefinition;


    public MarkLogicCriteriaReader(BoundedSource source, RuntimeContainer container, MarkLogicInputProperties inputProperties) {
        super(source);
        this.container = container;
        this.inputProperties = inputProperties;
    }

    @Override
    public boolean start() throws IOException {
        MarkLogicSource currentSource = getCurrentSource();
        result = new Result();
        connectionClient = currentSource.connect(container);
        if (connectionClient == null) {
            return false;
        }
        docManager = connectionClient.newDocumentManager();

        boolean isDocContentFieldPresent = (inputProperties.outputSchema.schema.getValue().getFields().size() >= 2);
        if (isDocContentFieldPresent) {
            docContentField = inputProperties.outputSchema.schema.getValue().getFields().get(1);
        }
        docContentReader = new DocContentReader(docManager, inputProperties.outputSchema.schema.getValue(), docContentField);

        criteria = inputProperties.criteria.getValue();
        if (inputProperties.useQueryOption.getValue() && StringUtils.isNotEmpty(inputProperties.queryOptionName.getStringValue())) {
            prepareQueryOption();
        }
        queryManager = connectionClient.newQueryManager();
        stringQueryDefinition = (inputProperties.useQueryOption.getValue()) ?
                queryManager.newStringDefinition(inputProperties.queryOptionName.getValue()) : queryManager.newStringDefinition();

        stringQueryDefinition.setCriteria(criteria);

        searchHandle = new SearchHandle();
        queryManager.search(stringQueryDefinition, searchHandle);


        matchedDocuments = searchHandle.getTotalResults();

        pageSize = (inputProperties.pageSize.getValue() <= 0) ? matchedDocuments : inputProperties.pageSize.getValue();
        maxRetrieve = inputProperties.maxRetrieve.getValue(); //if < 0 - it will be ignored
        queryManager.setPageLength(pageSize);
        documentCounter = 1;

        readNextPage();

        return (matchedDocuments > 0);
    }

    private void prepareQueryOption() {
        QueryOptionsManager qryOptMgr = connectionClient.newServerConfigManager().newQueryOptionsManager();
        if (StringUtils.isNotEmpty(inputProperties.queryOptionLiterals.getValue())) {
            StringHandle strHandle = new StringHandle();
            switch (inputProperties.queryLiteralType.getValue()) {
                case "JSON": {
                    strHandle.withFormat(Format.JSON);
                    break;
                }
                case "XML": {
                    strHandle.withFormat(Format.XML);
                    break;
                }
            }

            strHandle.set(inputProperties.queryOptionLiterals.getValue());
            qryOptMgr.writeOptions(inputProperties.queryOptionName.getValue(), strHandle);
        }
    }

    @Override
    public boolean advance() throws IOException {
        if (pageCounter >= pageSize) {
            readNextPage();
            pageCounter = 0;
        }
        return maxRetrieve > 0 ?
                (documentCounter <= maxRetrieve) && (documentCounter <= matchedDocuments) :
                documentCounter <= matchedDocuments;
    }

    private void readNextPage() {
        queryManager.search(stringQueryDefinition, searchHandle, documentCounter);
        currentPage = searchHandle.getMatchResults();
    }

    @Override
    public IndexedRecord getCurrent() throws NoSuchElementException {
        ++documentCounter;
        MatchDocumentSummary currentSummary = currentPage[pageCounter];
        current = new GenericData.Record(inputProperties.outputSchema.schema.getValue());
        try {
            String docId = currentSummary.getUri();
            current = docContentReader.readDocument(docId);

            result.successCount++;
            pageCounter++;
            return current;
        } catch (Exception e) {
            throw new MarkLogicException(new MarkLogicErrorCode("Can't read document from MarkLogic database"), e);
        }
    }



    @Override
    public Instant getCurrentTimestamp() throws NoSuchElementException {
        return Instant.now();
    }

    @Override
    public void close() throws IOException {
        if (!inputProperties.connection.isReferencedConnectionUsed()) {
            connectionClient.release();
        }
    }

    @Override
    public MarkLogicSource getCurrentSource() {
        return (MarkLogicSource) super.getCurrentSource();
    }

    @Override
    public Map<String, Object> getReturnValues() {
        return result.toMap();
    }
}
