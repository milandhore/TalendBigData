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

import org.apache.avro.Schema;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.ISchemaListener;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.ComponentConstants;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.SchemaProperties;
import org.talend.components.marklogic.MarkLogicProvideConnectionProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.talend.daikon.properties.presentation.Widget.widget;

public class MarkLogicInputProperties extends FixedConnectorsComponentProperties implements MarkLogicProvideConnectionProperties {

    public MarkLogicConnectionProperties connection = new MarkLogicConnectionProperties("connection");

    public SchemaProperties inputSchema = new SchemaProperties("inputSchema"){
        public void afterSchema() {
            if (inputSchemaListener != null) {
                inputSchemaListener.afterSchema();
            }
        }
    };

    public SchemaProperties outputSchema = new SchemaProperties("outputSchema");

    public MarkLogicInputProperties(String name) {
        super(name);
    }

    protected transient PropertyPathConnector INCOMING_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "inputSchema");

    protected transient PropertyPathConnector OUTGOING_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "outputSchema");

    public Property<Boolean> criteriaSearch = PropertyFactory.newBoolean("criteriaSearch");
    public Property<String> criteria = PropertyFactory.newString("criteria");
    public Property<String> docIdColumn =  PropertyFactory.newString("docIdColumn");

    public Property<Integer> maxRetrieve = PropertyFactory.newInteger("maxRetrieve");
    public Property<Integer> pageSize = PropertyFactory.newInteger("pageSize");
    public Property<Boolean> useQueryOption = PropertyFactory.newBoolean("useQueryOption");
    public Property<String> queryLiteralType = PropertyFactory.newString("queryLiteralType");
    public Property<String> queryOptionName = PropertyFactory.newString("queryOptionName");
    public Property<String> queryOptionLiterals = PropertyFactory.newString("queryOptionLiterals");

    private ISchemaListener inputSchemaListener;

    @Override
    public void setupProperties() {
        super.setupProperties();
        connection.setupProperties();
        criteriaSearch.setRequired();
        criteriaSearch.setValue(true);
        criteria.setRequired();
        docIdColumn.setRequired();


        useQueryOption.setValue(false);

        maxRetrieve.setValue(-1);
        pageSize.setValue(10);

        queryLiteralType.setPossibleValues("XML", "JSON");
        queryLiteralType.setValue("XML");

        queryOptionLiterals.setTaggedValue(ComponentConstants.LINE_SEPARATOR_REPLACED_TO, " ");
        setupDefaultSchema(outputSchema);

        setInputSchemaListener(new ISchemaListener() { //TODO replace with lambda

            @Override
            public void afterSchema() {
                updateDocIdColumnPossibleValues();
                if (!"docId".equals(outputSchema.schema.getValue().getFields().get(0).name())) {
                    setupDefaultSchema(outputSchema);
                }
            }
        });
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (form.getName().equals(Form.MAIN) && connection != null) {
            for (Form childForm : connection.getForms()) {
                connection.refreshLayout(childForm);
            }
            if (!isPlainOutputConnectionMode()) {
                updateDocIdColumnPossibleValues();
            }

            form.getWidget(criteria).setVisible(isPlainOutputConnectionMode());
            form.getWidget(docIdColumn).setHidden(isPlainOutputConnectionMode());
        }

        if (form.getName().equals(Form.ADVANCED)) {
            boolean isCriteriaModeUsed = isPlainOutputConnectionMode();

            form.getWidget(pageSize).setVisible(isCriteriaModeUsed);
            form.getWidget(maxRetrieve).setVisible(isCriteriaModeUsed);
            form.getWidget(useQueryOption).setVisible(isCriteriaModeUsed);
            form.getWidget(queryLiteralType).setVisible(isCriteriaModeUsed && useQueryOption.getValue());
            form.getWidget(queryOptionName).setVisible(isCriteriaModeUsed && useQueryOption.getValue());
            form.getWidget(queryOptionLiterals).setVisible(isCriteriaModeUsed && useQueryOption.getValue());
        }
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(connection.getForm(Form.REFERENCE));
        mainForm.addRow(inputSchema.getForm(Form.REFERENCE));
        mainForm.getWidget(inputSchema).setHidden();
        Form inputSchemaForm = ((Form) mainForm.getWidget(inputSchema).getContent());
        ((Property)inputSchemaForm.getWidget(inputSchema.schema).getContent()).removeFlag(Property.Flags.HIDDEN);
        mainForm.addRow(outputSchema.getForm(Form.REFERENCE));
        mainForm.addRow(criteriaSearch);
        mainForm.addRow(criteria);
        mainForm.addColumn(widget(docIdColumn).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));

        Form advancedForm = new Form(this, Form.ADVANCED);
        advancedForm.addRow(maxRetrieve);
        advancedForm.addRow(pageSize);
        advancedForm.addRow(useQueryOption);
        advancedForm.addRow(widget(queryLiteralType).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        advancedForm.addColumn(queryOptionName);
        advancedForm.addRow(widget(queryOptionLiterals).setWidgetType(Widget.TEXT_AREA_WIDGET_TYPE));
    }

    void setupDefaultSchema(SchemaProperties schemaToSet) {
        Schema stringSchema = AvroUtils._string();
        Schema.Field docContentField = new Schema.Field("docContent", stringSchema, null, (Object) null, Schema.Field.Order.IGNORE);

        setupDefaultSchema(schemaToSet, docContentField);
    }

    private void setupDefaultSchema(SchemaProperties schemaToSet, Schema.Field docContentField) {
        Schema stringSchema = AvroUtils._string();

        // create Schema for MarkLogic
        Schema.Field docIdField = new Schema.Field("docId", stringSchema, null, (Object) null, Schema.Field.Order.ASCENDING);
        docIdField.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        Schema.Field newDocContentField = new Schema.Field("docContent", docContentField.schema(),null,(Object) null, Schema.Field.Order.IGNORE);
        List<Schema.Field> fields = new ArrayList<>();
        fields.add(docIdField);
        fields.add(newDocContentField);
        Schema initialSchema = Schema.createRecord("markLogic", null, null, false, fields);

        schemaToSet.schema.setValue(initialSchema);
    }

    public void afterUseQueryOption() {
        refreshLayout(getForm(Form.ADVANCED));
    }

    public void afterCriteriaSearch() {
        refreshLayout(getForm(Form.MAIN));
        refreshLayout(getForm(Form.ADVANCED));
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(OUTGOING_CONNECTOR);
        } else {
            return Collections.singleton(INCOMING_CONNECTOR);
        }
    }

    @Override
    public MarkLogicConnectionProperties getConnectionProperties() {
        return connection;
    }

    private boolean isPlainOutputConnectionMode() {
        return criteriaSearch.getValue();
    }

    private void updateDocIdColumnPossibleValues() {
        List<String> inputFields = new ArrayList<>();
        for (Schema.Field inputField: inputSchema.schema.getValue().getFields()) {
            inputFields.add(inputField.name());
        }
        docIdColumn.setPossibleValues(inputFields);
    }

    public void setInputSchemaListener(ISchemaListener schemaListener) {
        this.inputSchemaListener = schemaListener;
    }
}
