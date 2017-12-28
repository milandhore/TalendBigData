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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.jdbc.ComponentConstants;
import org.talend.components.jdbc.runtime.reader.JDBCInputReader;

/**
 * JDBC runtime execution object for input action
 *
 */
public class JDBCSource extends JDBCSourceOrSink implements BoundedSource {

    private static final long serialVersionUID = -9111994542816954024L;

    //TODO will allow to use different mapping for different env like dataprep and di
    @SuppressWarnings("rawtypes")
    @Override
    public BoundedReader createReader(RuntimeContainer container) {
        return new JDBCInputReader(container, this, properties);
    }

    @Override
    public List<? extends BoundedSource> splitIntoBundles(long desiredBundleSizeBytes, RuntimeContainer adaptor)
            throws Exception {
        List<BoundedSource> list = new ArrayList<>();
        list.add(this);
        return list;
    }

    @Override
    public long getEstimatedSizeBytes(RuntimeContainer adaptor) {
        return 0;
    }

    @Override
    public boolean producesSortedKeys(RuntimeContainer adaptor) {
        return false;
    }

    @Override
    public Connection connect(RuntimeContainer runtime) throws ClassNotFoundException, SQLException {
        String refComponentId = setting.getReferencedComponentId();
        // using another component's connection
        if (refComponentId != null && runtime != null) {
            Object existedConn = runtime.getComponentData(refComponentId, ComponentConstants.CONNECTION_KEY);
            if (existedConn == null) {
                throw new RuntimeException("Referenced component: " + refComponentId + " is not connected");
            }
            return (Connection) existedConn;
        }

        return JdbcRuntimeUtils.createConnection(setting);
    }

}
