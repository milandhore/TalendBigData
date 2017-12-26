package org.talend.components.batchmode;

import java.io.IOException;
import java.util.List;

import org.apache.avro.Schema;
import org.talend.components.api.component.runtime.SourceOrSink;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.ValidationResult;

public class BatchModeSourceOrSink implements SourceOrSink {

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer container) throws IOException {
        return null;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {
        return null;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        return ValidationResult.OK;
    }

    @Override
    public ValidationResult initialize(RuntimeContainer container, ComponentProperties properties) {
        return ValidationResult.OK;
    }
}
