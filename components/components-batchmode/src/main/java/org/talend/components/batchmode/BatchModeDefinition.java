package org.talend.components.batchmode;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.DependenciesReader;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.component.runtime.SimpleRuntimeInfo;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

public class BatchModeDefinition extends AbstractComponentDefinition {

    public static final String COMPONENT_NAME = "tBatchModeOutput";

    public static final String MAVEN_GROUP_ID = "org.talend.components";

    public static final String MAVEN_ARTIFACT_ID = "components-batchmode";

    public BatchModeDefinition() {
        super(COMPONENT_NAME, ExecutionEngine.DI);
    }

    @Override
    public String[] getFamilies() {
        return new String[] { "TCOMP" };
    }

    @Override
    public boolean isConditionalInputs() {
        return true;
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return BatchModeProperties.class;
    }

    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_TOTAL_RECORD_COUNT_PROP, RETURN_SUCCESS_RECORD_COUNT_PROP,
                RETURN_REJECT_RECORD_COUNT_PROP };
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties,
            ConnectorTopology connectorTopology) {
        return new SimpleRuntimeInfo(this.getClass().getClassLoader(),
                DependenciesReader.computeDependenciesFilePath(MAVEN_GROUP_ID, MAVEN_ARTIFACT_ID),
                BatchModeSink.class.getCanonicalName());
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.INCOMING, ConnectorTopology.INCOMING_AND_OUTGOING);
    }
}
