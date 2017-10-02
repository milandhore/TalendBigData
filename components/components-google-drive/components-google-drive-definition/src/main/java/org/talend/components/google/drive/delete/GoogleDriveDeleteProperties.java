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
package org.talend.components.google.drive.delete;

import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.google.drive.GoogleDriveComponentProperties;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class GoogleDriveDeleteProperties extends GoogleDriveComponentProperties {

    public enum DeleteMode {
        Name,
        Id
    }

    public Property<String> fileName = newString("fileName").setRequired();

    public Property<Boolean> useTrash = newBoolean("useTrash");

    public Property<String> fileId = newString("fileId").setRequired();

    public Property<DeleteMode> deleteMode = newEnum("deleteMode", DeleteMode.class);

    public GoogleDriveDeleteProperties(String name) {
        super(name);
    }

    public void setupProperties() {
        super.setupProperties();

        Schema schema = SchemaBuilder.builder().record(GoogleDriveDeleteDefinition.COMPONENT_NAME).fields() //
                .name(GoogleDriveDeleteDefinition.RETURN_FILE_ID)//
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true")//
                .type().nullable().stringType().noDefault() //
                .endRecord();
        schema.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        schemaMain.schema.setValue(schema);

        deleteMode.setPossibleValues(DeleteMode.values());
        deleteMode.setValue(DeleteMode.Name);
        fileName.setValue("");
        fileId.setValue("");
        useTrash.setValue(true);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(deleteMode);
        mainForm.addRow(fileName);
        mainForm.addRow(fileId);
        mainForm.addRow(useTrash);
        mainForm.addRow(schemaMain.getForm(Form.REFERENCE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (Form.MAIN.equals(form.getName())) {
            form.getWidget(fileName.getName()).setVisible(DeleteMode.Name.equals(deleteMode.getValue()));
            form.getWidget(fileId.getName()).setVisible(!DeleteMode.Name.equals(deleteMode.getValue()));
        }
    }

    public void afterDeleteMode() {
        refreshLayout(getForm(Form.MAIN));
    }
}
