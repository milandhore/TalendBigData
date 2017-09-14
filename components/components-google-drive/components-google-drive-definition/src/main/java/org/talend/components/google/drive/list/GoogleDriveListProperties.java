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
package org.talend.components.google.drive.list;

import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import java.util.Date;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.google.drive.GoogleDriveComponentProperties;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.presentation.Widget;
import org.talend.daikon.properties.property.Property;

public class GoogleDriveListProperties extends GoogleDriveComponentProperties {

    public Property<String> folderName = newString("folderName").setRequired();

    public Property<Boolean> includeSubDirectories = newBoolean("includeSubDirectories");

    public enum ListMode {
        Files,
        Directories,
        Both
    }

    public Property<ListMode> listMode = newEnum("listMode", ListMode.class);

    public Property<Boolean> includeTrashedFiles = newBoolean("includeTrashedFiles");

    /*
     * TODO new feature to add: orderBy [in main]
     * 
     * orderBy string A comma-separated list of sort keys. Valid keys are 'createdTime', 'folder', 'modifiedByMeTime',
     * 'modifiedTime', 'name', 'quotaBytesUsed', 'recency', 'sharedWithMeTime', 'starred', and 'viewedByMeTime'. Each
     * key sorts ascending by default, but may be reversed with the 'desc' modifier. Example usage:
     * ?orderBy=folder,modifiedTime desc,name. Please note that there is a current limitation for users with
     * approximately one million files in which the requested sort order is ignored.
     */

    /*
     * TODO new feature to add: spaces [in advanced]
     * 
     * spaces string A comma-separated list of spaces to query within the corpus. Supported values are 'drive',
     * 'appDataFolder' and 'photos'.
     */

    public GoogleDriveListProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        folderName.setValue("root");
        listMode.setPossibleValues(ListMode.values());
        listMode.setValue(ListMode.Files);

        Schema schema = SchemaBuilder.builder().record("GoogleDriveList").fields().name("id")
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault().name("name")
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault().name("mimeType")
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault().name("modifiedTime")
                .prop(SchemaConstants.TALEND_IS_LOCKED, "true")
                .prop(SchemaConstants.TALEND_COLUMN_PATTERN, "yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'")//
                .prop(SchemaConstants.JAVA_CLASS_FLAG, Date.class.getCanonicalName()) //
                .prop(SchemaConstants.TALEND_COLUMN_DB_LENGTH, "255")//
                .type(AvroUtils._logicalTimestamp()).noDefault() //
                .name("size").prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().longType().noDefault() //
                .name("kind").prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault() //
                .name("trashed").prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().booleanType().noDefault() //
                .name("parents").prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault() //
                .name("webViewLink").prop(SchemaConstants.TALEND_IS_LOCKED, "true").type().nullable().stringType().noDefault() //
                .endRecord();
        schema.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        schemaMain.schema.setValue(schema);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(folderName);
        mainForm.addRow(Widget.widget(listMode).setWidgetType(Widget.ENUMERATION_WIDGET_TYPE));
        mainForm.addRow(includeSubDirectories);
        mainForm.addRow(schemaMain.getForm(Form.REFERENCE));

        Form advancedForm = new Form(this, Form.ADVANCED);
        advancedForm.addRow(includeTrashedFiles);
    }

}
