package org.talend.components.google.drive.delete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.runtime.ExecutionEngine;

public class GoogleDriveDeleteDefinitionTest {

    GoogleDriveDeleteDefinition def;

    @Before
    public void setUp() throws Exception {
        def = new GoogleDriveDeleteDefinition();
    }

    @Test
    public void testGetRuntimeInfo() throws Exception {
        assertNotNull(def.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.OUTGOING));
    }

    @Test
    public void testGetSupportedConnectorTopologies() throws Exception {
        assertEquals(EnumSet.of(ConnectorTopology.OUTGOING, ConnectorTopology.NONE), def.getSupportedConnectorTopologies());
    }

    @Test
    public void testIsStartable() throws Exception {
        assertTrue(def.isStartable());
    }
}
