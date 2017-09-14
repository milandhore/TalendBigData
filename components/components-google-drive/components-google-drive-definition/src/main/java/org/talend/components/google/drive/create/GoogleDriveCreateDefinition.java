package org.talend.components.google.drive.create;

import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.google.drive.GoogleDriveComponentDefinition;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class GoogleDriveCreateDefinition extends GoogleDriveComponentDefinition {

    public static final String COMPONENT_NAME = "tGoogleDriveCreate"; //$NON-NLS-1$

    public static final String RETURN_PARENTFOLDERID = "parentFolderID"; //$NON-NLS-1$

    public static final String RETURN_NEWFOLDERID = "newFolderID"; //$NON-NLS-1$

    public static final Property<String> RETURN_PARENTFOLDERID_PROP = PropertyFactory.newString(RETURN_PARENTFOLDERID);

    public static final Property<String> RETURN_NEWFOLDERID_PROP = PropertyFactory.newString(RETURN_NEWFOLDERID);

    public GoogleDriveCreateDefinition() {
        super(COMPONENT_NAME);
        setupI18N(new Property<?>[] { RETURN_PARENTFOLDERID_PROP, RETURN_NEWFOLDERID_PROP });
    }

    @Override
    public Property[] getReturnProperties() {
        return new Property[] { RETURN_ERROR_MESSAGE_PROP, RETURN_PARENTFOLDERID_PROP, RETURN_NEWFOLDERID_PROP };
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return GoogleDriveCreateProperties.class;
    }

}
