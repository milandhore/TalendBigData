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

import static org.talend.daikon.properties.presentation.Widget.widget;
import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import java.util.HashSet;
import java.util.Set;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.google.drive.GoogleDriveComponentProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;

public class GoogleDrivePutProperties extends GoogleDriveComponentProperties {

    public Property<String> fileName = newString("fileName").setRequired();

    public Property<String> destinationFolder = newString("destinationFolder").setRequired();

    public Property<Boolean> overwrite = newBoolean("overwrite");

    public enum UploadMode {
        READ_CONTENT_FROM_INPUT,
        UPLOAD_LOCAL_FILE,
        EXPOSE_OUTPUT_STREAM
    }

    public Property<UploadMode> uploadMode = newEnum("uploadMode", UploadMode.class);

    public Property<String> localFilePath = newString("localFilePath");

    public GoogleDrivePutProperties(String name) {
        super(name);
    }

    @Override
    public Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        Set<PropertyPathConnector> connectors = new HashSet<>();
        connectors.add(MAIN_CONNECTOR);
        return connectors;
    }

    public void setupProperties() {
        super.setupProperties();

        Schema schema = SchemaBuilder.builder().record("GoogleDrivePut").fields() //
                .name("content").type(AvroUtils._bytes()).noDefault() //
                .name("name").type().nullable().stringType().noDefault()//
                .name("fileID").type().nullable().stringType().noDefault()//
                .endRecord();
        // schema.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        schemaMain.schema.setValue(schema);

        fileName.setValue("");
        destinationFolder.setValue("");
        uploadMode.setPossibleValues(UploadMode.values());
        uploadMode.setValue(UploadMode.READ_CONTENT_FROM_INPUT);
        localFilePath.setValue("");
        overwrite.setValue(false);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(fileName);
        mainForm.addRow(destinationFolder);
        mainForm.addRow(overwrite);
        mainForm.addRow(uploadMode);
        mainForm.addRow(widget(localFilePath).setWidgetType(Widget.FILE_WIDGET_TYPE));
        mainForm.addRow(schemaMain.getForm(Form.REFERENCE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        form.getWidget(localFilePath.getName()).setVisible(UploadMode.UPLOAD_LOCAL_FILE.equals(uploadMode.getValue()));
    }

    public void afterUploadMode() {
        refreshLayout(getForm(Form.MAIN));
    }
}
