package org.talend.components.marklogic.runtime.input;

import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.component.runtime.Writer;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.tmarklogicinput.MarkLogicInputProperties;

import java.util.Map;

public class MarkLogicInputWriteOperation implements WriteOperation<Result> {

    private MarkLogicInputSink inputSink;

    private MarkLogicInputProperties inputProperties;

    public MarkLogicInputWriteOperation(MarkLogicInputSink markLogicInputSink, MarkLogicInputProperties inputProperties) {
        this.inputSink = markLogicInputSink;
        this.inputProperties = inputProperties;
    }

    @Override
    public void initialize(RuntimeContainer adaptor) {
        //nothing to do here
    }

    @Override
    public Map<String, Object> finalize(Iterable<Result> readResults, RuntimeContainer adaptor) {
        return Result.accumulateAndReturnMap(readResults);
    }

    @Override
    public MarkLogicRowProcessor createWriter(RuntimeContainer adaptor) {
        //Row reader is actually writer which takes docId and write docId and docContent result as feedback
        return new MarkLogicRowProcessor(this, adaptor, inputProperties);
    }

    @Override
    public MarkLogicInputSink getSink() {
        return inputSink;
    }
}
