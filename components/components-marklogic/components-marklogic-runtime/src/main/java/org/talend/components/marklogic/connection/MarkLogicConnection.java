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
package org.talend.components.marklogic.connection;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.exception.ExceptionContext.ExceptionContextBuilder;
import org.talend.daikon.exception.error.DefaultErrorCode;
import org.talend.daikon.exception.error.ErrorCode;
import org.talend.daikon.i18n.GlobalI18N;
import org.talend.daikon.i18n.I18nMessages;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.DatabaseClientFactory.SecurityContext;
import com.marklogic.client.FailedRequestException;

/**
 * Common implementation for creating or getting connection to MarkLogic database.
 *
 */
public abstract class MarkLogicConnection {

    private transient static final Logger LOGGER = LoggerFactory.getLogger(MarkLogicConnection.class);

    private static final I18nMessages MESSAGES = GlobalI18N.getI18nMessageProvider()
            .getI18nMessages(MarkLogicConnection.class);

    public static final String CONNECTION = "connection";

    private static final String ERROR_KEY = "message";

    /**
     * Creates new {@link DatabaseClient} connection or gets referenced one from container.
     *
     * @param container
     * @return connection to MarkLogic database.
     * @throws IOException thrown if referenced connection is not connected.
     */
    public DatabaseClient connect(RuntimeContainer container) {
        MarkLogicConnectionProperties properties = getMarkLogicConnectionProperties();
        if (properties.getReferencedComponentId() != null && container != null) {
            DatabaseClient client = (DatabaseClient) container.getComponentData(container.getCurrentComponentId(), CONNECTION);
            if (client != null) {
                return client;
            }

            throw new ComponentException(new DefaultErrorCode(400, ERROR_KEY),
                    new ExceptionContextBuilder().put(ERROR_KEY,
                            MESSAGES.getMessage("error.invalid.referenceConnection", properties.getReferencedComponentId()))
                            .build());
        }

        SecurityContext context = "BASIC".equals(properties.authentication.getValue())
                ? new DatabaseClientFactory.BasicAuthContext(properties.username.getValue(), properties.password.getValue())
                : new DatabaseClientFactory.DigestAuthContext(properties.username.getValue(), properties.password.getValue());
        DatabaseClient client = DatabaseClientFactory.newClient(properties.host.getValue(), properties.port.getValue(),
                properties.database.getValue(), context);

        testConnection(client);

        LOGGER.info("Connected to MarkLogic server");
        if (container != null) {
            container.setComponentData(container.getCurrentComponentId(), CONNECTION, client);
            LOGGER.info("Connection stored in container");
        }
        return client;
    }

    private void testConnection(DatabaseClient client) {
        try {
            // Since creating client is not enough for verifying established connection, need to make fake call.
            client.openTransaction().commit();
        } catch (Exception e) {
            ErrorCode errorCode;
            String message;
            if (e instanceof FailedRequestException) {
                errorCode = new DefaultErrorCode(403, ERROR_KEY);
                message = MESSAGES.getMessage("error.invalid.credentials");
            } else {
                errorCode = new DefaultErrorCode(400, ERROR_KEY);
                message = MESSAGES.getMessage("error.server.notReachable");
            }
            throw new ComponentException(errorCode, e, new ExceptionContextBuilder().put(ERROR_KEY, message).build());
        }
    }

    protected abstract MarkLogicConnectionProperties getMarkLogicConnectionProperties();

}
