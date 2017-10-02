package org.talend.components.google.drive.wizard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.wizard.ComponentWizard;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.components.google.drive.connection.GoogleDriveConnectionProperties;

public class GoogleDriveConnectionWizard extends ComponentWizard {

    GoogleDriveConnectionProperties connection;

    /**
     * This shall be called by the service creation and is not supposed to be called by any client.
     *
     * @param definition wizard definition
     * @param repositoryLocation store location
     */
    public GoogleDriveConnectionWizard(ComponentWizardDefinition definition, String repositoryLocation) {
        super(definition, repositoryLocation);

        connection = new GoogleDriveConnectionProperties("connection").setRepositoryLocation(getRepositoryLocation());
        connection.init();
        addForm(connection.getForm(GoogleDriveConnectionProperties.FORM_WIZARD));
    }

    private transient static final Logger LOG = LoggerFactory.getLogger(GoogleDriveConnectionWizard.class);

    public void setupProperties(GoogleDriveConnectionProperties connectionProperties) {
        LOG.warn("[setupProperties] {}", connectionProperties);
        connection.copyValuesFrom(connectionProperties);
    }

    public boolean supportsProperties(ComponentProperties properties) {
        LOG.warn("[supportsProperties] {}", properties);
        return properties instanceof GoogleDriveConnectionProperties;
    }

}
