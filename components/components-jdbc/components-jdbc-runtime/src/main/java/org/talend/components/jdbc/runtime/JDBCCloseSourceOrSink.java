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
package org.talend.components.jdbc.runtime;

import java.sql.SQLException;

import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.jdbc.ComponentConstants;
import org.talend.components.jdbc.RuntimeSettingProvider;
import org.talend.components.jdbc.runtime.setting.JdbcRuntimeSourceOrSinkDefault;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.ValidationResultMutable;

/**
 * JDBC close runtime execution object
 *
 */
public class JDBCCloseSourceOrSink extends JdbcRuntimeSourceOrSinkDefault {

    private static final long serialVersionUID = 1L;

    public ComponentProperties properties;

    @Override
    public ValidationResult initialize(RuntimeContainer runtime, ComponentProperties properties) {
        this.properties = properties;
        return ValidationResult.OK;
    }

    @Override
    public ValidationResult validate(RuntimeContainer runtime) {
        ValidationResultMutable vr = new ValidationResultMutable();
        try {
            doCloseAction(runtime);
        } catch (Exception ex) {
            vr.setStatus(Result.ERROR);
            vr.setMessage(ex.getMessage());
        }
        return vr;
    }

    public void doCloseAction(RuntimeContainer runtime) throws SQLException {
        String refComponentId = ((RuntimeSettingProvider) properties).getRuntimeSetting().getReferencedComponentId();
        if (refComponentId != null && runtime != null) {
            java.sql.Connection conn = (java.sql.Connection) runtime.getComponentData(ComponentConstants.CONNECTION_KEY,
                    refComponentId);
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } else {
            throw new RuntimeException("Can't find the connection by the key");
        }
    }

}
