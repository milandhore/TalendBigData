package org.talend.components.google.drive;

import org.talend.components.api.AbstractComponentFamilyDefinition;
import org.talend.components.api.ComponentInstaller;
import org.talend.components.api.Constants;
import org.talend.components.google.drive.connection.GoogleDriveConnectionDefinition;
import org.talend.components.google.drive.copy.GoogleDriveCopyDefinition;
import org.talend.components.google.drive.create.GoogleDriveCreateDefinition;
import org.talend.components.google.drive.delete.GoogleDriveDeleteDefinition;
import org.talend.components.google.drive.get.GoogleDriveGetDefinition;
import org.talend.components.google.drive.list.GoogleDriveListDefinition;
import org.talend.components.google.drive.put.GoogleDrivePutDefinition;

import aQute.bnd.annotation.component.Component;

@Component(name = Constants.COMPONENT_INSTALLER_PREFIX + GoogleDriveFamilyDefinition.NAME, provide = ComponentInstaller.class)
public class GoogleDriveFamilyDefinition extends AbstractComponentFamilyDefinition implements ComponentInstaller {

    public static final String NAME = "Google Drive";

    public GoogleDriveFamilyDefinition() {
        super(NAME, new GoogleDriveConnectionDefinition(), new GoogleDriveCreateDefinition(), new GoogleDriveDeleteDefinition(),
                new GoogleDriveListDefinition(), new GoogleDriveGetDefinition(), new GoogleDrivePutDefinition(),
                new GoogleDriveCopyDefinition());
    }

    @Override
    public void install(ComponentFrameworkContext ctx) {
        ctx.registerComponentFamilyDefinition(this);
    }
}
