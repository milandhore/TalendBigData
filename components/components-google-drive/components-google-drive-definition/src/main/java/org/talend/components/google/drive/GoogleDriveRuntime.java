package org.talend.components.google.drive;

import org.talend.components.google.drive.connection.GoogleDriveConnectionProperties;
import org.talend.daikon.properties.ValidationResult;

public interface GoogleDriveRuntime {

    /**
     * Validate connection for given connection properties.
     *
     * @param properties connection properties
     * @return result of validation
     */
    ValidationResult validateConnection(GoogleDriveConnectionProperties properties);
}
