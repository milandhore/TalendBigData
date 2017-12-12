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
package org.talend.components.marklogic.data;

import java.util.ArrayList;
import java.util.List;

import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.common.datastore.runtime.DatastoreRuntime;
import org.talend.components.marklogic.runtime.TMarkLogicConnectionStandalone;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.properties.ValidationResult;

/**
 *
 *
 */
public class MarkLogicDatastoreRuntime implements DatastoreRuntime<MarkLogicConnectionProperties> {

    private static final long serialVersionUID = -7454667988635055372L;

    private MarkLogicConnectionProperties properties;

    @Override
    public ValidationResult initialize(RuntimeContainer container, MarkLogicConnectionProperties properties) {
        this.properties = properties;
        return ValidationResult.OK;
    }

    @Override
    public Iterable<ValidationResult> doHealthChecks(RuntimeContainer container) {
        List<ValidationResult> checks = new ArrayList<>(1);
        TMarkLogicConnectionStandalone standalone = new TMarkLogicConnectionStandalone();
        try {
            standalone.initialize(container, properties);
            standalone.connect(container);
            checks.add(ValidationResult.OK);
        } catch (ComponentException ce) {
            checks.add(new ValidationResult(ce));
        }
        return checks;
    }
}
