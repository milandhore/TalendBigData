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

import static org.talend.daikon.properties.presentation.Widget.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.avro.Schema;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.marklogic.dataset.MarkLogicDatasetProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class MarkLogicInputProperties extends FixedConnectorsComponentProperties {

    public MarkLogicConnectionProperties connection = new MarkLogicConnectionProperties("connection");

    public MarkLogicDatasetProperties datasetProperties = new MarkLogicDatasetProperties("datasetProperties");

    public MarkLogicInputProperties(String name) {
        super(name);
    }

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "datasetProperties.main");

    public Property<String> criteria = PropertyFactory.newString("criteria");

    public Property<Integer> maxRetrieve = PropertyFactory.newInteger("maxRetrieve");
    public Property<Integer> pageSize = PropertyFactory.newInteger("pageSize");
    public Property<Boolean> useQueryOption = PropertyFactory.newBoolean("useQueryOption");
    public Property<String> queryLiteralType = PropertyFactory.newString("queryLiteralType");
    public Property<String> queryOptionName = PropertyFactory.newString("queryOptionName");
    public Property<String> queryOptionLiterals = PropertyFactory.newString("queryOptionLiterals");

    @Override
    public void setupProperties() {
        super.setupProperties();
        connection.setupProperties();
        datasetProperties.setDatastoreProperties(connection);

        criteria.setRequired();

        useQueryOption.setValue(false);

        maxRetrieve.setValue(-1);
        pageSize.setValue(10);

        queryLiteralType.setPossibleValues("XML", "JSON");
        queryLiteralType.setValue("XML");
        setupSchema();
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (form.getName().equals(Form.MAIN) && connection != null) {
            for (Form childForm : connection.getForms()) {
                connection.refreshLayout(childForm);
            }
        }

        if (form.getName().equals(Form.ADVANCED)) {
           form.getWidget(queryLiteralType).setVisible(useQueryOption);
           form.getWidget(queryOptionName).setVisible(useQueryOption);
           form.getWidget(queryOptionLiterals).setVisible(useQueryOption);
        }
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.MAIN));
        mainForm.addRow(datasetProperties.getForm(Form.REFERENCE));
        mainForm.addRow(criteria);

        Form advancedForm = new Form(this, Form.ADVANCED);
        advancedForm.addRow(maxRetrieve);
        advancedForm.addRow(pageSize);
        advancedForm.addRow(useQueryOption);
        advancedForm.addRow(widget(queryLiteralType).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        advancedForm.addColumn(queryOptionName);
        advancedForm.addRow(widget(queryOptionLiterals).setWidgetType(Widget.TEXT_AREA_WIDGET_TYPE));
    }

    void setupSchema() {
        Schema stringSchema = AvroUtils._string();

        // create Schema for MarkLogic
        Schema.Field docIdField = new Schema.Field("docId", stringSchema, null, (Object) null, Schema.Field.Order.ASCENDING);
        docIdField.addProp(SchemaConstants.TALEND_COLUMN_IS_KEY, "true");
        docIdField.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        Schema.Field docContentField = new Schema.Field("docContent", stringSchema, null, (Object) null, Schema.Field.Order.IGNORE);
        List<Schema.Field> fields = new ArrayList<>();
        fields.add(docIdField);
        fields.add(docContentField);
        Schema initialSchema = Schema.createRecord("jira", null, null, false, fields);

        datasetProperties.main.schema.setValue(initialSchema);
    }

    public void afterUseQueryOption() {
        refreshLayout(getForm(Form.ADVANCED));
    }
    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(MAIN_CONNECTOR);
        }
        return Collections.emptySet();
    }
}
