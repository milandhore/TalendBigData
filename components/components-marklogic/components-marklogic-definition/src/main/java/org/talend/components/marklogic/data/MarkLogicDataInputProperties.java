package org.talend.components.marklogic.data;

import static org.talend.daikon.properties.presentation.Widget.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.avro.Schema;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.ComponentConstants;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.io.IOProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

/**
 *
 *
 */
public class MarkLogicDataInputProperties extends FixedConnectorsComponentProperties
        implements IOProperties<MarkLogicDatasetProperties> {

    public ReferenceProperties<MarkLogicDatasetProperties> dataset = new ReferenceProperties<>("dataset",
            MarkLogicDatasetDefinition.NAME);

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME,
            "dataset.main");

    public Property<String> criteria = PropertyFactory.newString("criteria");
    public Property<Integer> pageSize = PropertyFactory.newInteger("pageSize");
    public Property<Boolean> useQueryOption = PropertyFactory.newBoolean("useQueryOption");
    public Property<String> queryLiteralType = PropertyFactory.newString("queryLiteralType");
    public Property<String> queryOptionName = PropertyFactory.newString("queryOptionName");
    public Property<String> queryOptionLiterals = PropertyFactory.newString("queryOptionLiterals");

    public MarkLogicDataInputProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        useQueryOption.setValue(false);
        pageSize.setValue(10);
        queryLiteralType.setPossibleValues("XML", "JSON");
        queryLiteralType.setValue("XML");
        queryOptionLiterals.setTaggedValue(ComponentConstants.LINE_SEPARATOR_REPLACED_TO, " ");
        setupSchema();
    }

    private void setupSchema () {
        Schema.Field docIdField = new Schema.Field("docId", AvroUtils._string(), null, (Object) null, Schema.Field.Order.ASCENDING);
        docIdField.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        Schema.Field newDocContentField = new Schema.Field("docContent", AvroUtils._string(), null,(Object) null, Schema.Field.Order.IGNORE);
        List<Schema.Field> fields = new ArrayList<>();
        fields.add(docIdField);
        fields.add(newDocContentField);
        Schema initialSchema = Schema.createRecord("markLogic", null, null, false, fields);
        getDatasetProperties().main.schema.setValue(initialSchema);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        // May not be present in dataset properties. May be tested only using datacatalog.
        mainForm.addRow(getDatasetProperties().getDatastoreProperties().getForm(Form.REFERENCE));
        mainForm.addRow(getDatasetProperties().getForm(Form.REFERENCE));
        mainForm.addRow(criteria);
        mainForm.addRow(pageSize);
        mainForm.addRow(useQueryOption);
        mainForm.addRow(widget(queryLiteralType).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        mainForm.addColumn(queryOptionName);
        mainForm.addRow(widget(queryOptionLiterals).setWidgetType(Widget.TEXT_AREA_WIDGET_TYPE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        form.getWidget(queryLiteralType).setVisible(useQueryOption.getValue());
        form.getWidget(queryOptionName).setVisible(useQueryOption.getValue());
        form.getWidget(queryOptionLiterals).setVisible(useQueryOption.getValue());
    }

    @Override
    public MarkLogicDatasetProperties getDatasetProperties() {
        return dataset.getReference();
    }

    @Override
    public void setDatasetProperties(MarkLogicDatasetProperties datasetProperties) {
        dataset.setReference(datasetProperties);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(MAIN_CONNECTOR);
        } else {
            return Collections.emptySet();
        }
    }

}
