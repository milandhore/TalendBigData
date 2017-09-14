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
package org.talend.components.google.drive.copy;

import static org.talend.daikon.properties.property.PropertyFactory.newBoolean;
import static org.talend.daikon.properties.property.PropertyFactory.newEnum;
import static org.talend.daikon.properties.property.PropertyFactory.newString;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.talend.components.google.drive.GoogleDriveComponentProperties;
import org.talend.daikon.avro.SchemaConstants;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class GoogleDriveCopyProperties extends GoogleDriveComponentProperties {

    public enum CopyMode {
        File,
        Folder
    }

    public Property<CopyMode> copyMode = newEnum("copyMode", CopyMode.class);

    public Property<String> fileName = newString("fileName");

    public Property<String> folderName = newString("folderName");

    public Property<String> destinationFolder = newString("destinationFolder");

    public Property<Boolean> rename = newBoolean("rename");

    public Property<String> newName = newString("newName");

    public Property<Boolean> deleteSourceFile = newBoolean("deleteSourceFile");

    public GoogleDriveCopyProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();

        copyMode.setPossibleValues(CopyMode.values());
        copyMode.setValue(CopyMode.File);

        rename.setValue(false);
        deleteSourceFile.setValue(false);

        Schema s = SchemaBuilder.builder().record("GoogleDriveCopy").fields()//
                .name("sourceID").type().nullable().stringType().noDefault()//
                .name("destinationID").type().nullable().stringType().noDefault()//
                .endRecord();
        s.addProp(SchemaConstants.TALEND_IS_LOCKED, "true");
        schemaMain.schema.setValue(s);
    }

    @Override
    public void setupLayout() {
        super.setupLayout();

        Form mainForm = getForm(Form.MAIN);
        mainForm.addRow(copyMode);
        mainForm.addRow(fileName);
        mainForm.addRow(folderName);
        mainForm.addRow(destinationFolder);
        mainForm.addRow(rename);
        mainForm.addRow(newName);
        mainForm.addRow(deleteSourceFile);
        mainForm.addRow(schemaMain.getForm(Form.REFERENCE));
    }

    @Override
    public void refreshLayout(Form form) {
        super.refreshLayout(form);

        if (CopyMode.File.equals(copyMode.getValue())) {
            form.getWidget(fileName.getName()).setVisible(true);
            form.getWidget(folderName.getName()).setVisible(false);
            form.getWidget(deleteSourceFile.getName()).setVisible(true);
        } else {
            form.getWidget(fileName.getName()).setVisible(false);
            form.getWidget(folderName.getName()).setVisible(true);
            form.getWidget(deleteSourceFile.getName()).setVisible(false);
        }
        form.getWidget(newName.getName()).setVisible(rename.getValue());
    }

    public void afterCopyMode() {
        refreshLayout(getForm(Form.MAIN));
    }

    public void afterRename() {
        refreshLayout(getForm(Form.MAIN));
    }

}
