package org.talend.components.google.drive.get;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;

public class GoogleDriveGetDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveGet";

    public GoogleDriveGetDefinition() {
        super(COMPONENT_NAME);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return GoogleDriveGetProperties.class;
    }

}
