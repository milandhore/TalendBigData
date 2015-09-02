// ============================================================================
//
// Copyright (C) 2006-2015 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.salesforce;

import org.talend.salesforce.SforceBasicConnection;

public class SalesforceConnection {

    public void connect(SalesforceConnectionProperties properties) throws Exception {

        SforceBasicConnection.Builder builder = new SforceBasicConnection.Builder(properties.url.getValue(),
                properties.userPassword.userId.getValue(), properties.userPassword.password.getValue());
        if (properties.timeout.getValue() > 0)
            builder.setTimeout(properties.timeout.getValue());
        if (properties.needCompression.getValue() != null)
            builder.needCompression(properties.needCompression.getValue());
        if (properties.clientId.getValue() != null)
            builder.setClientID(properties.clientId.getValue());

        SforceBasicConnection sforceConn = builder.build();
        System.out.println("conn: " + sforceConn);
    }
}
