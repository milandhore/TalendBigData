package org.talend.components.batchmode;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.util.List;

import org.apache.avro.Schema;
import org.slf4j.Logger;
import org.talend.components.api.component.runtime.Sink;
import org.talend.components.api.component.runtime.WriteOperation;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.ValidationResult;

public class BatchModeSink extends BatchModeSourceOrSink implements Sink {

    private BatchModeProperties properties;

    private transient static final Logger LOG = getLogger(BatchModeSink.class);

    @Override
    public WriteOperation<?> createWriteOperation() {
        return new BatchModeWriteOperation(this);
    }

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer container) throws IOException {
        LOG.debug("[getSchemaNames]");
        return null;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {
        LOG.debug("[getEndpointSchema] schemaName: {}.", schemaName);
        return null;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {
        LOG.debug("[validate]");
        return ValidationResult.OK;
    }

    @Override
    public ValidationResult initialize(RuntimeContainer container, ComponentProperties properties) {
        LOG.debug("[initialize] properties: {}", properties);
        this.properties = (BatchModeProperties) properties;
        return ValidationResult.OK;
    }

    public BatchModeProperties getProperties() {
        return properties;
    }

}
