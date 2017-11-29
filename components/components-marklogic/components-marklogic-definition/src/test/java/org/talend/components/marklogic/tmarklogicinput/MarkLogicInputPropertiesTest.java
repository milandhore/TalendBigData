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
package org.talend.components.marklogic.tmarklogicinput;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionDefinition;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionPropertiesTest;
import org.talend.daikon.properties.presentation.Form;

import java.util.ArrayList;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.talend.daikon.avro.SchemaConstants.TALEND_IS_LOCKED;

public class MarkLogicInputPropertiesTest {

    private MarkLogicInputProperties testInputProperties;

    @Before
    public void setUp() {
        testInputProperties = new MarkLogicInputProperties("testInputProperties");
    }

    /**
     * Checks forms are filled with required widgets
     */
    @Test
    public void testSetupLayout() {
        testInputProperties.connection.init();
        testInputProperties.inputSchema.init();
        testInputProperties.outputSchema.init();

        testInputProperties.setupLayout();
        Form main = testInputProperties.getForm(Form.MAIN);
        assertNotNull(main.getWidget(testInputProperties.connection));
        assertNotNull(main.getWidget(testInputProperties.criteria));
        //should not be on main form
        assertNull(main.getWidget(testInputProperties.maxRetrieve));
        assertNull(main.getWidget(testInputProperties.pageSize));
        assertNull(main.getWidget(testInputProperties.useQueryOption));
        assertNull(main.getWidget(testInputProperties.queryLiteralType));
        assertNull(main.getWidget(testInputProperties.queryOptionName));
        assertNull(main.getWidget(testInputProperties.queryOptionLiterals));

        Form advanced = testInputProperties.getForm(Form.ADVANCED);
        assertNotNull(advanced.getWidget(testInputProperties.maxRetrieve));
        assertNotNull(advanced.getWidget(testInputProperties.pageSize));
        assertNotNull(advanced.getWidget(testInputProperties.useQueryOption));
        assertNotNull(advanced.getWidget(testInputProperties.queryLiteralType));
        assertNotNull(advanced.getWidget(testInputProperties.queryOptionName));
        assertNotNull(advanced.getWidget(testInputProperties.queryOptionLiterals));
    }

    /**
     * Checks default values are set correctly
     */
    @Test
    public void testSetupProperties() {
        Integer expectedDefaultMaxRetrieveNumber = -1;
        Integer expectedDefaultPageSize = 10;
        Boolean expectedDefaultUseQueryOption = false;
        String expectedDefaultQueryLiteralType = "XML";

        testInputProperties.setupProperties();

        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_HOST, testInputProperties.connection.host.getValue());
        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_PORT, testInputProperties.connection.port.getValue());
        assertEquals(MarkLogicConnectionPropertiesTest.EXPECTED_DEFAULT_DATABASE, testInputProperties.connection.database.getValue());
        assertNull(testInputProperties.connection.username.getValue());
        assertNull(testInputProperties.connection.password.getValue());
        assertNull(testInputProperties.criteria.getValue());
        assertEquals(expectedDefaultMaxRetrieveNumber, testInputProperties.maxRetrieve.getValue());
        assertEquals(expectedDefaultPageSize, testInputProperties.pageSize.getValue());
        assertEquals(expectedDefaultUseQueryOption, testInputProperties.useQueryOption.getValue());
        assertEquals(expectedDefaultQueryLiteralType, testInputProperties.queryLiteralType.getValue());
        assertNull(testInputProperties.queryOptionName.getValue());
        assertNull(testInputProperties.queryOptionLiterals.getValue());
    }

    @Test
    public void testSchemaDocIdFieldIsLocked() {
        testInputProperties.setupDefaultSchema(testInputProperties.inputSchema);
        testInputProperties.setupDefaultSchema(testInputProperties.outputSchema);
        assertNull(testInputProperties.outputSchema.schema.getValue().getProp(TALEND_IS_LOCKED));
        assertEquals("true", testInputProperties.outputSchema.schema.getValue().getField("docId").getProp(TALEND_IS_LOCKED));
        assertNull(testInputProperties.outputSchema.schema.getValue().getField("docContent").getProp(TALEND_IS_LOCKED));
    }

    @Test
    public void testGetAllSchemaPropertiesConnectorsForIOConnection() {
        Set<PropertyPathConnector> actualConnectors = testInputProperties.getAllSchemaPropertiesConnectors(true);

        assertThat(actualConnectors, Matchers.contains(testInputProperties.OUTGOING_CONNECTOR));
        assertEquals(1,actualConnectors.size());
        assertEquals("outputSchema", new ArrayList<>(actualConnectors).get(0).getPropertyPath());
    }

    @Test
    public void testGetAllSchemaPropertiesConnectorsForOnlyOutputConnection() {
        Set<PropertyPathConnector> actualConnectors = testInputProperties.getAllSchemaPropertiesConnectors(false);

        assertThat(actualConnectors, Matchers.contains(testInputProperties.INCOMING_CONNECTOR));
        assertEquals(1, actualConnectors.size());
        assertEquals("inputSchema", new ArrayList<>(actualConnectors).get(0).getPropertyPath());
    }



    /**
     * Checks initial layout
     */
    @Test
    public void testRefreshLayout() {
        testInputProperties.init();
        testInputProperties.refreshLayout(testInputProperties.getForm(Form.MAIN));
        testInputProperties.refreshLayout(testInputProperties.getForm(Form.ADVANCED));

        boolean schemaHidden = testInputProperties.getForm(Form.MAIN).getWidget("outputSchema").isHidden();
        boolean isConnectionPropertiesHidden = testInputProperties.getForm(Form.MAIN).getWidget("connection").isHidden();
        boolean isQueryCriteriaHidden = testInputProperties.getForm(Form.MAIN).getWidget("criteria").isHidden();
        boolean isMaxRetrieveHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("maxRetrieve").isHidden();
        boolean isPageSizeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("pageSize").isHidden();
        boolean isUseQueryOptionHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("useQueryOption").isHidden();
        boolean isQueryLiteralTypeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryLiteralType").isHidden();
        boolean isQueryOptionNameHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryOptionName").isHidden();
        boolean isQueryOptionLiteralsHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryOptionLiterals").isHidden();

        assertFalse(schemaHidden);
        assertFalse(isConnectionPropertiesHidden);
        assertFalse(isQueryCriteriaHidden);
        assertFalse(isMaxRetrieveHidden);
        assertFalse(isPageSizeHidden);
        assertFalse(isUseQueryOptionHidden);

        assertTrue(isQueryLiteralTypeHidden);
        assertTrue(isQueryOptionNameHidden);
        assertTrue(isQueryOptionLiteralsHidden);
    }

    @Test
    public void testUseExistedConnectionHideConnectionWidget() {
        MarkLogicConnectionProperties someConnection = new MarkLogicConnectionProperties("connection");

        testInputProperties.init();
        someConnection.init();
        testInputProperties.connection.referencedComponent.setReference(someConnection);
        testInputProperties.connection.referencedComponent.componentInstanceId.setValue(MarkLogicConnectionDefinition.COMPONENT_NAME + "_1");
        testInputProperties.refreshLayout(testInputProperties.getForm(Form.MAIN));

        boolean isConnectionHostPropertyHidden = testInputProperties.connection.getForm(Form.MAIN).getWidget(testInputProperties.connection.host).isHidden();
        boolean isConnectionPortPropertyHidden = testInputProperties.connection.getForm(Form.MAIN).getWidget(testInputProperties.connection.port).isHidden();
        boolean isUserNameHidden = testInputProperties.connection.getForm(Form.MAIN).getWidget(testInputProperties.connection.username).isHidden();
        boolean isPasswordHidden = testInputProperties.connection.getForm(Form.MAIN).getWidget(testInputProperties.connection.password).isHidden();
        boolean isConnectionDatabasePropertyHidden = testInputProperties.connection.getForm(Form.MAIN).getWidget(testInputProperties.connection.database).isHidden();

        assertTrue(isConnectionHostPropertyHidden);
        assertTrue(isConnectionPortPropertyHidden);
        assertTrue(isUserNameHidden);
        assertTrue(isPasswordHidden);
        assertTrue(isConnectionDatabasePropertyHidden);
    }

    @Test
    public void testAfterUseQueryOption() {
        testInputProperties.init();

        testInputProperties.useQueryOption.setValue(true);
        testInputProperties.afterUseQueryOption();

        boolean isQueryLiteralTypeVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryLiteralType).isVisible();
        boolean isQueryOptionNameVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionName).isVisible();
        boolean isQueryLiteralsVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionLiterals).isVisible();

        assertTrue(isQueryLiteralTypeVisible);
        assertTrue(isQueryOptionNameVisible);
        assertTrue(isQueryLiteralsVisible);
    }

    @Test
    public void testAdvancedPropertiesVisibleForCriteriaMode() {
        testInputProperties.init();
        testInputProperties.criteriaSearch.setValue(false);
        testInputProperties.afterCriteriaSearch();

        boolean isMaxRetrieveHidden = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.maxRetrieve).isHidden();
        boolean isPageSizeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.pageSize).isHidden();
        boolean isQueryLiteralTypeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryLiteralType).isHidden();
        boolean isQueryOptionNameHidden = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionName).isHidden();
        boolean isQueryLiteralsHidden = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionLiterals).isHidden();

        assertTrue(isMaxRetrieveHidden);
        assertTrue(isPageSizeHidden);
        assertTrue(isQueryLiteralTypeHidden);
        assertTrue(isQueryOptionNameHidden);
        assertTrue(isQueryLiteralsHidden);
    }

    @Test
    public void testGetConnectionProperties() {
        MarkLogicConnectionProperties connectionProperties = new MarkLogicConnectionProperties("connectionProperties");
        connectionProperties.init();
        testInputProperties.init();
        testInputProperties.connection.referencedComponent.setReference(connectionProperties);

        assertEquals(connectionProperties, testInputProperties.getConnectionProperties());
    }
}
