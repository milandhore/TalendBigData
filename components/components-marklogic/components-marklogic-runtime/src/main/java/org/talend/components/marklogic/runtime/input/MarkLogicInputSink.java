package org.talend.components.marklogic.runtime.input;

import org.talend.components.api.component.runtime.Sink;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.runtime.MarkLogicSourceOrSink;
import org.talend.components.marklogic.tmarklogicinput.MarkLogicInputProperties;
import org.talend.daikon.properties.ValidationResult;

public class MarkLogicInputSink extends MarkLogicSourceOrSink implements Sink {

    @Override
    public MarkLogicInputWriteOperation createWriteOperation() {
        return new MarkLogicInputWriteOperation(this, (MarkLogicInputProperties) ioProperties);
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        if (ioProperties instanceof MarkLogicInputProperties) {
            checkDocContentTypeSupported(((MarkLogicInputProperties) ioProperties).datasetProperties.main);
        } else {
            return new ValidationResult(ValidationResult.Result.ERROR, MESSAGES.getMessage("error.wrongProperties"));
        }
        return super.validate(container);
    }
}
