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
package org.talend.components.google.drive.create;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;
import org.talend.components.google.drive.connection.GoogleDriveConnectionDefinition;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;
import org.talend.daikon.runtime.RuntimeInfo;

public class GoogleDriveCreateDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveCreate"; //$NON-NLS-1$

    public static final String RETURN_PARENTFOLDERID = "parentFolderID"; //$NON-NLS-1$

    public static final String RETURN_NEWFOLDERID = "newFolderID"; //$NON-NLS-1$

    public static final Property<String> RETURN_PARENTFOLDERID_PROP = PropertyFactory.newString(RETURN_PARENTFOLDERID);

    public static final Property<String> RETURN_NEWFOLDERID_PROP = PropertyFactory.newString(RETURN_NEWFOLDERID);

    private transient static final Logger LOG = LoggerFactory.getLogger(GoogleDriveCreateDefinition.class);

    public GoogleDriveCreateDefinition() {
        super(COMPONENT_NAME);
        setupI18N(new Property<?>[] { RETURN_PARENTFOLDERID_PROP, RETURN_NEWFOLDERID_PROP });
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties,
            ConnectorTopology connectorTopology) {
        assertEngineCompatibility(engine);
        assertConnectorTopologyCompatibility(connectorTopology);
        if (ConnectorTopology.NONE.equals(connectorTopology)) {
            return getRuntimeInfo(GoogleDriveConnectionDefinition.CREATE_RUNTIME_CLASS);
        } else {
            return getRuntimeInfo(GoogleDriveConnectionDefinition.SOURCE_CLASS);
        }
    }

    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_PARENTFOLDERID_PROP, RETURN_NEWFOLDERID_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return GoogleDriveCreateProperties.class;
    }

}
