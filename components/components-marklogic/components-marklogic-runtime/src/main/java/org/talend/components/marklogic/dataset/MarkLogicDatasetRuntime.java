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
package org.talend.components.marklogic.dataset;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.common.dataset.runtime.DatasetRuntime;
import org.talend.daikon.java8.Consumer;
import org.talend.daikon.properties.ValidationResult;

/**
 *
 *
 */
public class MarkLogicDatasetRuntime implements DatasetRuntime<MarkLogicDatasetProperties> {

    private MarkLogicDatasetProperties properties;

    private RuntimeContainer container;

    @Override
    public ValidationResult initialize(RuntimeContainer container, MarkLogicDatasetProperties properties) {
        this.properties = properties;
        this.container = container;
        return ValidationResult.OK;
    }

    @Override
    public Schema getSchema() {
        return properties.main.schema.getValue();
    }

    @Override
    public void getSample(int limit, Consumer<IndexedRecord> consumer) {

    }

}
