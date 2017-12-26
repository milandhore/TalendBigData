package org.talend.components.batchmode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.WriterWithFeedback;
import org.talend.components.api.container.RuntimeContainer;

public class BatchModeWriter implements WriterWithFeedback<Result, IndexedRecord, IndexedRecord> {

    private final boolean useNMRows;

    private final Integer mFactor;

    private final Boolean generateErrors;

    private final Random randomGenerator;

    private final Boolean useBatch;

    private final Integer batchSize;

    private final String traceString;

    private RuntimeContainer container;

    private WriteOperation operation;

    private Result result;

    private List<IndexedRecord> successfulWrites = new ArrayList<>();

    private List<IndexedRecord> rejectedWrites = new ArrayList<>();

    private List<IndexedRecord> records = new ArrayList<>();

    private Schema flowSchema;

    private Schema writeSchema;

    private Schema rejectSchema;

    private transient static final Logger LOG = LoggerFactory.getLogger(BatchModeWriter.class);

    public BatchModeWriter(RuntimeContainer adaptor, BatchModeWriteOperation batchModeWriteOperation) {
        randomGenerator = new Random();
        this.container = adaptor;
        operation = batchModeWriteOperation;
        BatchModeSink sink = (BatchModeSink) operation.getSink();
        // writeSchema = sink.getProperties().schema.schema.getValue();
        flowSchema = sink.getProperties().schemaFlow.schema.getValue();
        rejectSchema = sink.getProperties().schemaReject.schema.getValue();
        useBatch = sink.getProperties().useBatch.getValue();
        batchSize = useBatch ? sink.getProperties().batchSize.getValue() : 1;
        useNMRows = sink.getProperties().useNMRows.getValue();
        mFactor = sink.getProperties().mFactor.getValue();
        generateErrors = sink.getProperties().generateErrors.getValue();
        traceString = sink.getProperties().traceString.getValue();
    }

    @Override
    public WriteOperation<Result> getWriteOperation() {
        return operation;
    }

    @Override
    public void open(String uId) throws IOException {
        this.result = new Result(uId);
        LOG.debug("[open] batchSize: {};", batchSize);
    }

    @Override
    public void write(Object object) throws IOException {
        if (object == null) {
            LOG.debug("[write] Incoming data is null. Skipping...");
            return;
        }
        //
        result.totalCount++;
        IndexedRecord inputRecord = (IndexedRecord) object;
        LOG.debug("[write] Pending records: {}; success: {}; errors: {}; coming record→{}.", records.size(),
                successfulWrites.size(), rejectedWrites.size(), inputRecord);
        // This for dynamic which would get schema from the first record
        if (writeSchema == null) {
            writeSchema = ((IndexedRecord) object).getSchema();
        }
        records.add(inputRecord);
        if (records.size() == batchSize) {
            LOG.debug("[write] Sending records...");
            sendRecords();
        }
    }

    @Override
    public Result close() throws IOException {
        if (records.size() > 0) {
            LOG.debug("[close] We still have {} records to process...", records.size());
            sendRecords();
        }
        LOG.debug("[close] {}", result);
        return result;
    }

    @Override
    public Iterable<IndexedRecord> getSuccessfulWrites() {
        LOG.debug("[getSuccessfulWrites] {} →{}", successfulWrites.size(), successfulWrites);
        return Collections.unmodifiableCollection(successfulWrites);
    }

    @Override
    public Iterable<IndexedRecord> getRejectedWrites() {
        LOG.debug("[getRejectedWrites] {} →{}", rejectedWrites.size(), rejectedWrites);
        return Collections.unmodifiableCollection(rejectedWrites);
    }

    private void sendRecords() {
        cleanWrites();
        int nbRecords = 1;
        for (IndexedRecord record : records) {
            if (useNMRows) {
                nbRecords = randomGenerator.nextInt(mFactor);
                nbRecords = nbRecords == 0 ? 1 : nbRecords;
                LOG.debug("[generateRecords] 1 row creating {} records.", nbRecords);
            }
            for (int i = 0; i < nbRecords; i++) {
                int rndErr = randomGenerator.nextInt(100);
                if (generateErrors && (rndErr < 30)) {
                    handleReject(record, new RuntimeException("Generated Error"), rndErr);
                } else {
                    handleSuccess(record);
                }
            }
        }
        // cleanup records list
        records.clear();
    }

    public void cleanWrites() {
        successfulWrites.clear();
        rejectedWrites.clear();
    }

    private void handleSuccess(IndexedRecord record) {
        result.successCount++;
        if (writeSchema == null || writeSchema.getFields().isEmpty()) {
            return;
        }

        IndexedRecord success = new GenericData.Record(flowSchema);
        for (Schema.Field outField : success.getSchema().getFields()) {
            Object outValue;
            Schema.Field inField = record.getSchema().getField(outField.name());
            if (inField != null) {
                outValue = record.get(inField.pos());
                if (outValue instanceof String) {
                    outValue = traceString + outValue;
                }
                success.put(outField.pos(), outValue);
            }
        }
        successfulWrites.add(success);
    }

    private void handleReject(IndexedRecord record, Exception e, int errorCode) {
        result.rejectCount++;
        if (rejectSchema == null || rejectSchema.getFields().isEmpty()) {
            LOG.debug("debug.NoRejectSchema");
            return;
        }
        if (record.getSchema().equals(rejectSchema)) {
            rejectedWrites.add(record);
        } else {
            IndexedRecord reject = new GenericData.Record(rejectSchema);
            reject.put(rejectSchema.getField("errorCode").pos(), String.valueOf(errorCode));
            reject.put(rejectSchema.getField("errorMessage").pos(), e.getLocalizedMessage());
            for (Schema.Field outField : reject.getSchema().getFields()) {
                Object outValue;
                Schema.Field inField = record.getSchema().getField(outField.name());
                if (inField != null) {
                    outValue = record.get(inField.pos());
                    reject.put(outField.pos(), outValue);
                }
            }
            rejectedWrites.add(reject);
        }
    }
}
