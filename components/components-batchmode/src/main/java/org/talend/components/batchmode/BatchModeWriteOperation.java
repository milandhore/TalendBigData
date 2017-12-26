package org.talend.components.batchmode;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.Sink;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.Writer;
import org.talend.components.api.container.RuntimeContainer;

class BatchModeWriteOperation implements WriteOperation<Result> {

    private BatchModeSink sink;

    private transient static final Logger LOG = LoggerFactory.getLogger(BatchModeWriteOperation.class);

    public BatchModeWriteOperation(BatchModeSink sink) {
        this.sink = sink;
    }

    @Override
    public void initialize(RuntimeContainer adaptor) {
        //
    }

    @Override
    public Writer<Result> createWriter(RuntimeContainer adaptor) {
        return new BatchModeWriter(adaptor, this);
    }

    @Override
    public Map<String, Object> finalize(Iterable<Result> results, RuntimeContainer adaptor) {
        LOG.debug("[finalize] results: {} accumulate {}", results, Result.accumulateAndReturnMap(results));
        return Result.accumulateAndReturnMap(results);
    }

    @Override
    public Sink getSink() {
        return sink;
    }
}
