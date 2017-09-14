package org.talend.components.google.drive.delete;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class GoogleDriveDeleteDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveDelete";

    public static final String RETURN_FILEID = "fileID"; //$NON-NLS-1$

    public static final Property<String> RETURN_FILEID_PROP = PropertyFactory.newString(RETURN_FILEID);

    public GoogleDriveDeleteDefinition() {
        super(COMPONENT_NAME);
        setupI18N(new Property<?>[] { RETURN_FILEID_PROP });
    }

    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_FILEID_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return GoogleDriveDeleteProperties.class;
    }

}
