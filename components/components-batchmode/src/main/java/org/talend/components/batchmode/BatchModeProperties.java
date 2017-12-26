package org.talend.components.batchmode;

import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newInteger;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.SchemaBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.component.Connector;
import org.talend.components.api.component.ISchemaListener;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.SchemaProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class BatchModeProperties extends FixedConnectorsComponentProperties {

    public Property<String> traceString = newString("traceString");

    public Property<Boolean> useBatch = newBoolean("useBatch");

    public Property<Integer> batchSize = newInteger("batchSize");

    public Property<Boolean> useNMRows = newBoolean("useNMRows");

    public Property<Boolean> generateErrors = newBoolean("generateErrors");

    public Property<Integer> mFactor = newInteger("mFactor");

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schema");

    protected transient PropertyPathConnector FLOW_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME, "schemaFlow");

    protected transient PropertyPathConnector REJECT_CONNECTOR = new PropertyPathConnector(Connector.REJECT_NAME, "schemaReject");

    public ISchemaListener schemaListener;

    public SchemaProperties schema = new SchemaProperties("schema") {

        @SuppressWarnings("unused")
        public void afterSchema() {
            LOG.debug("[afterSchema] schema");
            if (schemaListener != null) {
                schemaListener.afterSchema();
            }
        }

    };

    public SchemaProperties schemaFlow = new SchemaProperties("schemaFlow"); //$NON-NLS-1$

    public SchemaProperties schemaReject = new SchemaProperties("schemaReject"); //$NON-NLS-1$

    private transient static final Logger LOG = LoggerFactory.getLogger(BatchModeProperties.class);

    public BatchModeProperties(String name) {
        super(name);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        HashSet<PropertyPathConnector> connectors = new HashSet<>();
        if (isOutputConnection) {
            connectors.add(FLOW_CONNECTOR);
            connectors.add(REJECT_CONNECTOR);
        } else {
            connectors.add(MAIN_CONNECTOR);
        }
        LOG.debug("[getAllSchemaPropertiesConnectors] {} → {}", isOutputConnection, connectors);
        return connectors;
    }

    public void setSchemaListener(ISchemaListener schemaListener) {
        this.schemaListener = schemaListener;
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        LOG.debug("[setupProperties]");
        traceString.setValue("dbg_");
        useBatch.setValue(false);
        batchSize.setValue(20);
        useNMRows.setValue(false);
        generateErrors.setValue(false);
        mFactor.setValue(2);
        Schema s = SchemaBuilder.record("Main").fields()
                //
                .name("PartitionKey").prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault()
                //
                .name("RowKey").prop(SchemaConstants.TALEND_COLUMN_IS_KEY, "true")
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")// $NON-NLS-3$
                .type(AvroUtils._string()).noDefault()
                //
                .endRecord();
        schema.schema.setValue(s);
        // update the properties when schema change
        setSchemaListener(new ISchemaListener() {

            @Override
            public void afterSchema() {
                updateOutputSchemas();
            }
        });

    }

    @Override
    public void setupLayout() {
        super.setupLayout();
        Form mainForm = new Form(this, Form.MAIN);
        mainForm.addRow(schema.getForm(Form.REFERENCE));
        mainForm.addRow(traceString);
        mainForm.addRow(useBatch);
        mainForm.addColumn(batchSize);
        mainForm.addRow(useNMRows);
        mainForm.addColumn(mFactor);
        mainForm.addRow(generateErrors);
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);
        LOG.debug("[refreshLayout] {} ", form);
        if (Form.MAIN.equals(form.getName())) {
            form.getWidget(batchSize.getName()).setVisible(useBatch.getValue());
            form.getWidget(mFactor.getName()).setVisible(useNMRows.getValue());
        }
    }

    public void afterUseBatch() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void afterUseNMRows() {
        refreshLayout(getForm(Form.MAIN));
    }

    protected void updateOutputSchemas() {
        Schema inputSchema = schema.schema.getValue();
        schemaFlow.schema.setValue(inputSchema);
        final List<Field> additionalRejectFields = new ArrayList<Field>();

        Schema.Field field = null;
        field = new Schema.Field("errorCode", Schema.create(Schema.Type.STRING), null, (Object) null);
        field.addProp(SchemaConstants.TALEND_IS_LOCKED, "false");
        field.addProp(SchemaConstants.TALEND_FIELD_GENERATED, "true");
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255");
        additionalRejectFields.add(field);

        field = new Schema.Field("errorMessage", Schema.create(Schema.Type.STRING), null, (Object) null);
        field.addProp(SchemaConstants.TALEND_IS_LOCKED, "false");
        field.addProp(SchemaConstants.TALEND_FIELD_GENERATED, "true");
        field.addProp(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255");
        additionalRejectFields.add(field);

        Schema rejectSchema = newSchema(inputSchema, "schemaReject", additionalRejectFields);
        schemaReject.schema.setValue(rejectSchema);
    }

    private Schema newSchema(Schema metadataSchema, String newSchemaName, List<Schema.Field> moreFields) {
        Schema newSchema = Schema.createRecord(newSchemaName, metadataSchema.getDoc(), metadataSchema.getNamespace(),
                metadataSchema.isError());

        List<Schema.Field> copyFieldList = new ArrayList<>();
        for (Schema.Field se : metadataSchema.getFields()) {
            Schema.Field field = new Schema.Field(se.name(), se.schema(), se.doc(), se.defaultVal(), se.order());
            field.getObjectProps().putAll(se.getObjectProps());
            for (Map.Entry<String, Object> entry : se.getObjectProps().entrySet()) {
                field.addProp(entry.getKey(), entry.getValue());
            }
            copyFieldList.add(field);
        }

        copyFieldList.addAll(moreFields);

        newSchema.setFields(copyFieldList);
        for (Map.Entry<String, Object> entry : metadataSchema.getObjectProps().entrySet()) {
            newSchema.addProp(entry.getKey(), entry.getValue());
        }
        return newSchema;
    }
}
