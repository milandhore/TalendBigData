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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.talend.daikon.avro.SchemaConstants.TALEND_IS_LOCKED;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionDefinition;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionPropertiesTest;
import org.talend.daikon.properties.presentation.Form;

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
        testInputProperties.setupProperties();
        testInputProperties.connection.init();
        testInputProperties.datasetProperties.init();
        testInputProperties.datasetProperties.main.init();

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
        testInputProperties.setupSchema();
        assertNull(testInputProperties.datasetProperties.main.schema.getValue().getProp(TALEND_IS_LOCKED));
        assertEquals("true", testInputProperties.datasetProperties.main.schema.getValue().getField("docId").getProp(TALEND_IS_LOCKED));
        assertNull(testInputProperties.datasetProperties.main.schema.getValue().getField("docContent").getProp(TALEND_IS_LOCKED));
    }

    @Test
    public void testGetAllSchemaPropertiesConnectors() {
        Set<PropertyPathConnector> actualConnectors = testInputProperties.getAllSchemaPropertiesConnectors(true);

        assertThat(actualConnectors, Matchers.contains(testInputProperties.MAIN_CONNECTOR));
    }

    @Test
    public void testGetAllSchemaPropertiesConnectorsForWrongConnection() {
        Set<PropertyPathConnector> actualConnectors = testInputProperties.getAllSchemaPropertiesConnectors(false);

        assertThat(actualConnectors, empty());
    }



    /**
     * Checks initial layout
     */
    @Test
    public void testRefreshLayout() {
        testInputProperties.setupProperties();
        testInputProperties.connection.init();
        testInputProperties.datasetProperties.init();
        testInputProperties.setupLayout();

        testInputProperties.refreshLayout(testInputProperties.getForm(Form.MAIN));
        testInputProperties.refreshLayout(testInputProperties.getForm(Form.ADVANCED));

        boolean isConnectionPropertiesHidden = testInputProperties.getForm(Form.MAIN).getWidget("connection").isHidden();
        boolean isQueryCriteriaHidden = testInputProperties.getForm(Form.MAIN).getWidget("criteria").isHidden();
        boolean isMaxRetrieveHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("maxRetrieve").isHidden();
        boolean isPageSizeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("pageSize").isHidden();
        boolean isUseQueryOptionHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("useQueryOption").isHidden();
        boolean isQueryLiteralTypeHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryLiteralType").isHidden();
        boolean isQueryOptionNameHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryOptionName").isHidden();
        boolean isQueryOptionLiteralsHidden = testInputProperties.getForm(Form.ADVANCED).getWidget("queryOptionLiterals").isHidden();

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

        testInputProperties.setupProperties();
        testInputProperties.connection.init();
        testInputProperties.datasetProperties.init();
        testInputProperties.setupLayout();

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
        testInputProperties.setupProperties();
        testInputProperties.connection.init();
        testInputProperties.datasetProperties.init();
        testInputProperties.setupLayout();

        testInputProperties.useQueryOption.setValue(true);
        testInputProperties.afterUseQueryOption();

        boolean isQueryLiteralTypeVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryLiteralType).isVisible();
        boolean isQueryOptionNameVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionName).isVisible();
        boolean isQueryLiteralsVisible = testInputProperties.getForm(Form.ADVANCED).getWidget(testInputProperties.queryOptionLiterals).isVisible();

        assertTrue(isQueryLiteralTypeVisible);
        assertTrue(isQueryOptionNameVisible);
        assertTrue(isQueryLiteralsVisible);
    }
}
