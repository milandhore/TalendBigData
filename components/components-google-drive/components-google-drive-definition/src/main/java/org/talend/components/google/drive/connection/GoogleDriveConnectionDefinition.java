package org.talend.components.google.drive.connection;

import java.util.EnumSet;
import java.util.Set;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;
import org.talend.daikon.runtime.RuntimeInfo;

public class GoogleDriveConnectionDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveConnection"; //$NON-NLS-1$

    public GoogleDriveConnectionDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.NONE);
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties, ConnectorTopology connectorTopology) {
        assertEngineCompatibility(engine);
        assertConnectorTopologyCompatibility(connectorTopology);
        return getRuntimeInfo(GoogleDriveConnectionDefinition.SOURCE_OR_SINK_CLASS);
    }

    @Override
    public boolean isStartable() {
        return true;
    }
}
