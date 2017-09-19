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
package org.talend.components.google.drive.get;

import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;
import org.talend.components.google.drive.connection.GoogleDriveConnectionDefinition;
import org.talend.daikon.runtime.RuntimeInfo;

public class GoogleDriveGetDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveGet";

    public GoogleDriveGetDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties,
            ConnectorTopology connectorTopology) {
        assertEngineCompatibility(engine);
        assertConnectorTopologyCompatibility(connectorTopology);
        if (ConnectorTopology.NONE.equals(connectorTopology)) {
            return getRuntimeInfo(GoogleDriveConnectionDefinition.GET_RUNTIME_CLASS);
        } else {
            return getRuntimeInfo(GoogleDriveConnectionDefinition.SOURCE_CLASS);
        }
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return GoogleDriveGetProperties.class;
    }

}
