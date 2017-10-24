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

import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.components.common.SchemaProperties;
import org.talend.components.common.dataset.DatasetProperties;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionDefinition;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.presentation.Form;


public class MarkLogicDatasetProperties extends ComponentPropertiesImpl implements DatasetProperties<MarkLogicConnectionProperties> {

    public ReferenceProperties<MarkLogicConnectionProperties> datastore = new ReferenceProperties<>("datastore", MarkLogicConnectionDefinition.COMPONENT_NAME);

    public SchemaProperties main = new SchemaProperties("main");

    public MarkLogicDatasetProperties(String name) {
        super(name);
    }

    @Override
    public void setupProperties() {
        super.setupProperties();
    }

    @Override
    public void setupLayout() {

        Form referenceForm = Form.create(this, Form.REFERENCE);
        referenceForm.addRow(main.getForm(Form.REFERENCE));
    }

    @Override
    public MarkLogicConnectionProperties getDatastoreProperties() {
        return datastore.getReference();
    }

    @Override
    public void setDatastoreProperties(MarkLogicConnectionProperties datastoreProperties) {
        datastore.setReference(datastoreProperties);
    }

}
