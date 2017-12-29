package org.talend.components.marklogic.runtime.input;

import com.marklogic.client.DatabaseClient;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.WriterWithFeedback;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.exceptions.MarkLogicErrorCode;
import org.talend.components.marklogic.exceptions.MarkLogicException;
import org.talend.components.marklogic.runtime.input.strategies.DocContentReader;
import org.talend.components.marklogic.tmarklogicinput.MarkLogicInputProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is runtime reader for MarkLogic, which get document content for provided docId at each iteration.
 * IndexedRecord {docId, docContent} is sent as feedback to output row
 * Reject row should be empty
 */
public class MarkLogicRowProcessor implements WriterWithFeedback<Result, IndexedRecord, IndexedRecord>{

    private MarkLogicInputProperties inputProperties;

    private MarkLogicInputWriteOperation inputWriteOperation;
    private String uId;
    private RuntimeContainer container;

    private DatabaseClient client;

    private List<IndexedRecord> documents;

    DocContentReader docContentReader;

    private int totalCounter;

    public MarkLogicRowProcessor(MarkLogicInputWriteOperation markLogicInputWriteOperation, RuntimeContainer container, MarkLogicInputProperties properties) {
        this.inputWriteOperation = markLogicInputWriteOperation;
        this.container = container;
        this.inputProperties = properties;
        this.documents = new ArrayList<>();
    }

    @Override
    public Iterable<IndexedRecord> getSuccessfulWrites() {
        return documents;
    }

    @Override
    public Iterable<IndexedRecord> getRejectedWrites() {
        return Collections.emptySet();
    }

    @Override
    public void open(String uId) throws IOException {
        this.uId = uId;
        client = inputWriteOperation.getSink().connect(container);

        Schema.Field docContentField = inputProperties.outputSchema.schema.getValue().getFields().get(1);
        docContentReader = new DocContentReader(client.newDocumentManager(), inputProperties.outputSchema.schema.getValue(), docContentField);
    }

    @Override
    public void write(Object indexedRecordDatum) throws IOException {
        if (indexedRecordDatum == null) {
            return;
        }
        documents.clear();
        IndexedRecord indexedRecord = (IndexedRecord) indexedRecordDatum;
        Schema indexedRecordSchema = indexedRecord.getSchema();

        int docIdFieldIndex = indexedRecordSchema.getFields().indexOf(indexedRecordSchema.getField(inputProperties.docIdColumn.getStringValue()));
        if (docIdFieldIndex == -1) {
            throw new MarkLogicException(new MarkLogicErrorCode("Can't find docId column "
                    + inputProperties.docIdColumn.getStringValue() + " in input row"));
        }
        String docId = (String) indexedRecord.get(docIdFieldIndex);
        GenericData.Record matchedDocument = docContentReader.readDocument(docId);
        totalCounter++;

        documents.add(matchedDocument);
    }

    @Override
    public Result close() throws IOException {
        if (!inputProperties.connection.isReferencedConnectionUsed()) {
            client.release();
        }
        return new Result(uId, totalCounter);
    }

    @Override
    public WriteOperation<Result> getWriteOperation() {
        return inputWriteOperation;
    }
}
