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
package org.talend.components.marklogic.tmarklogicoutput;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionDefinition;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionPropertiesTest;
import org.talend.daikon.properties.presentation.Form;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.talend.daikon.avro.SchemaConstants.TALEND_IS_LOCKED;

public class MarkLogicOutputPropertiesTest {
    private MarkLogicOutputProperties testOutputProperties;

    @Before
    public void setUp() {
        testOutputProperties = new MarkLogicOutputProperties("outputProperties");
    }

    @Test
    public void testSetupLayout() {
        testOutputProperties.connection.init();
        testOutputProperties.schema.init();

        testOutputProperties.setupLayout();

        Form main = testOutputProperties.getForm(Form.MAIN);
        assertNotNull(main.getWidget(testOutputProperties.connection));
        assertNotNull(main.getWidget(testOutputProperties.action));
        //should not be on main form
        assertNull(main.getWidget(testOutputProperties.docType));
        assertNull(main.getWidget(testOutputProperties.autoGenerateDocId));
        assertNull(main.getWidget(testOutputProperties.docIdPrefix));

        Form advanced = testOutputProperties.getForm(Form.ADVANCED);
        assertNotNull(advanced.getWidget(testOutputProperties.docType));
        assertNotNull(advanced.getWidget(testOutputProperties.autoGenerateDocId));
        assertNotNull(advanced.getWidget(testOutputProperties.docIdPrefix));
    }

    @Test
    public void testSetupProperties() {
        String expectedDefaultAction = "UPSERT";
        String expectedDefaultDocType = "MIXED";
        Boolean expectedDefaultAutoGenerateDocId = false;
        String expectedDefaultDocIdPrefix = "/";

        testOutputProperties.setupProperties();

        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_HOST, testOutputProperties.connection.host.getValue());
        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_PORT, testOutputProperties.connection.port.getValue());
        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_DATABASE, testOutputProperties.connection.database.getValue());
        assertNull(testOutputProperties.connection.username.getValue());
        assertNull(testOutputProperties.connection.password.getValue());
        assertEquals(expectedDefaultAction, testOutputProperties.action.getValue());
        assertEquals(expectedDefaultDocType, testOutputProperties.docType.getValue());
        assertEquals(expectedDefaultAutoGenerateDocId, testOutputProperties.autoGenerateDocId.getValue());
        assertEquals(expectedDefaultDocIdPrefix, testOutputProperties.docIdPrefix.getValue());
    }

    @Test
    public void testMainSchemaIsLocked() {
        testOutputProperties.setupSchema();
        assertEquals("true", testOutputProperties.schema.schema.getValue().getProp(TALEND_IS_LOCKED));
    }

    @Test
    public void testRejectSchemaIsLocked() {
        testOutputProperties.setupSchema();
        assertEquals("true", testOutputProperties.schemaReject.schema.getValue().getProp(TALEND_IS_LOCKED));
    }

    @Test
    public void testRefreshLayout() {
        testOutputProperties.init();
        testOutputProperties.refreshLayout(testOutputProperties.getForm(Form.MAIN));
        testOutputProperties.refreshLayout(testOutputProperties.getForm(Form.ADVANCED));
        boolean schemaHidden = testOutputProperties.getForm(Form.MAIN).getWidget("schema").isHidden();
        boolean isConnectionPropertiesHidden = testOutputProperties.getForm(Form.MAIN).getWidget("connection").isHidden();
        boolean isActionHidden = testOutputProperties.getForm(Form.MAIN).getWidget("action").isHidden();
        boolean isDocTypeHidden = testOutputProperties.getForm(Form.ADVANCED).getWidget("docType").isHidden();
        boolean isAutoGenerateDocIdHidden = testOutputProperties.getForm(Form.ADVANCED).getWidget("autoGenerateDocId").isHidden();
        boolean isDocIdPrefixHidden = testOutputProperties.getForm(Form.ADVANCED).getWidget("docIdPrefix").isHidden();

        assertFalse(schemaHidden);
        assertFalse(isConnectionPropertiesHidden);
        assertFalse(isActionHidden);
        assertFalse(isDocTypeHidden);

        assertTrue(isAutoGenerateDocIdHidden);
        assertTrue(isDocIdPrefixHidden);
    }

    @Test
    public void testUseExistedConnectionHideConnectionWidget() {
        MarkLogicConnectionProperties someConnection = new MarkLogicConnectionProperties("connection");

        testOutputProperties.init();
        someConnection.init();
        testOutputProperties.connection.referencedComponent.setReference(someConnection);
        testOutputProperties.connection.referencedComponent.componentInstanceId.setValue(
                MarkLogicConnectionDefinition.COMPONENT_NAME + "_1");
        testOutputProperties.refreshLayout(testOutputProperties.getForm(Form.MAIN));

        boolean isConnectionHostPropertyHidden = testOutputProperties.connection.getForm(Form.MAIN).getWidget(testOutputProperties.connection.host).isHidden();
        boolean isConnectionPortPropertyHidden = testOutputProperties.connection.getForm(Form.MAIN).getWidget(testOutputProperties.connection.port).isHidden();
        boolean isUserNameHidden = testOutputProperties.connection.getForm(Form.MAIN).getWidget(testOutputProperties.connection.username).isHidden();
        boolean isPasswordHidden = testOutputProperties.connection.getForm(Form.MAIN).getWidget(testOutputProperties.connection.password).isHidden();
        boolean isConnectionDatabasePropertyHidden = testOutputProperties.connection.getForm(Form.MAIN).getWidget(testOutputProperties.connection.database).isHidden();

        assertTrue(isConnectionHostPropertyHidden);
        assertTrue(isConnectionPortPropertyHidden);
        assertTrue(isUserNameHidden);
        assertTrue(isPasswordHidden);
        assertTrue(isConnectionDatabasePropertyHidden);
    }

    @Test
    public void testAfterDocType() {
        testOutputProperties.init();

        testOutputProperties.docType.setValue("PLAIN TEXT");
        testOutputProperties.afterDocType();
        assertTrue(isAutoGenerateDocIdPropertyVisible(testOutputProperties));

        testOutputProperties.docType.setValue("JSON");
        testOutputProperties.afterDocType();
        assertTrue(isAutoGenerateDocIdPropertyVisible(testOutputProperties));

        testOutputProperties.docType.setValue("XML");
        testOutputProperties.afterDocType();
        assertTrue(isAutoGenerateDocIdPropertyVisible(testOutputProperties));

        testOutputProperties.docType.setValue("BINARY");
        testOutputProperties.afterDocType();
        assertTrue(isAutoGenerateDocIdPropertyVisible(testOutputProperties));

        testOutputProperties.docType.setValue("MIXED");
        testOutputProperties.afterDocType();
        assertFalse(isAutoGenerateDocIdPropertyVisible(testOutputProperties));
    }

    @Test
    public void testAfterAutoGenerateDocId() {
        testOutputProperties.init();

        testOutputProperties.docType.setValue("PLAIN TEXT");
        testOutputProperties.autoGenerateDocId.setValue(true);
        testOutputProperties.afterDocType();
        testOutputProperties.afterAutoGenerateDocId();
        assertTrue(isDocIdPrefixPropertyVisible(testOutputProperties));

        testOutputProperties.autoGenerateDocId.setValue(false);
        testOutputProperties.afterAutoGenerateDocId();
        assertFalse(isDocIdPrefixPropertyVisible(testOutputProperties));

    }


    private boolean isAutoGenerateDocIdPropertyVisible(MarkLogicOutputProperties properties) {
        return properties.getForm(Form.ADVANCED).getWidget(properties.autoGenerateDocId).isVisible();
    }

    private boolean isDocIdPrefixPropertyVisible(MarkLogicOutputProperties properties) {
        return properties.getForm(Form.ADVANCED).getWidget(properties.docIdPrefix).isVisible();
    }

    @Test
    public void testGetAllSchemaPropertiesConnectors() {
        Set<PropertyPathConnector> actualConnectors = testOutputProperties.getAllSchemaPropertiesConnectors(false);

        assertThat(actualConnectors, contains(testOutputProperties.MAIN_CONNECTOR));
    }

    @Test
    public void testGetAllSchemaPropertiesConnectorsForFlowConnector() {
        Set<PropertyPathConnector> actualConnectors = testOutputProperties.getAllSchemaPropertiesConnectors(true);

        assertThat(actualConnectors, contains(testOutputProperties.FLOW_CONNECTOR, testOutputProperties.REJECT_CONNECTOR));
    }

}