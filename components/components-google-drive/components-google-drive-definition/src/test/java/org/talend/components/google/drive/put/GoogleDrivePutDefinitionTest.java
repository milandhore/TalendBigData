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
package org.talend.components.google.drive.put;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;

public class GoogleDrivePutDefinitionTest {

    GoogleDrivePutDefinition def;

    @Before
    public void setUp() throws Exception {
        def = new GoogleDrivePutDefinition();
    }

    @Test
    public void testGetRuntimeInfo() throws Exception {
        assertNotNull(def.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.NONE));
        assertNotNull(def.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.OUTGOING));
        assertNotNull(def.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.INCOMING_AND_OUTGOING));
        assertNotNull(def.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.INCOMING));
    }

    @Test
    public void testGetSupportedConnectorTopologies() throws Exception {
        assertEquals(EnumSet.of(ConnectorTopology.INCOMING, ConnectorTopology.INCOMING_AND_OUTGOING, ConnectorTopology.NONE,
                ConnectorTopology.OUTGOING), def.getSupportedConnectorTopologies());
    }
}
