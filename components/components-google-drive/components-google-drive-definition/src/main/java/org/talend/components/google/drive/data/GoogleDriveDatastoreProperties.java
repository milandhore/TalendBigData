package org.talend.components.google.drive.data;

import static org.talend.daikon.properties.property.PropertyFactory.newProperty;

import org.talend.components.common.datastore.DatastoreProperties;
import org.talend.daikon.properties.PropertiesImpl;
import org.talend.daikon.properties.presentation.Form;
import org.talend.daikon.properties.property.Property;

public class GoogleDriveDatastoreProperties extends PropertiesImpl implements DatastoreProperties {

    public Property<String> serviceAccountJSONFile = newProperty("serviceAccountJSONFile").setRequired();

    public GoogleDriveDatastoreProperties(String name) {
        super(name);
    }

    @Override
    public void setupLayout() {
        Form mainForm = Form.create(this, Form.MAIN);
        mainForm.addRow(serviceAccountJSONFile);
    }
}
