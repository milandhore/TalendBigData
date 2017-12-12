package org.talend.components.marklogic.data;

import java.util.Collections;
import java.util.Set;

import org.talend.components.api.component.Connector;
import org.talend.components.api.component.PropertyPathConnector;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.common.io.IOProperties;
import org.talend.daikon.properties.ReferenceProperties;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

/**
 *
 *
 */
public class MarkLogicDataInputProperties extends FixedConnectorsComponentProperties
        implements IOProperties<MarkLogicDatasetProperties> {

    public ReferenceProperties<MarkLogicDatasetProperties> dataset = new ReferenceProperties<>("dataset",
            MarkLogicDatasetDefinition.NAME);

    protected transient PropertyPathConnector MAIN_CONNECTOR = new PropertyPathConnector(Connector.MAIN_NAME,
            "dataset.main");

    public Property<String> criteria = PropertyFactory.newString("criteria");
    public Property<Integer> pageSize = PropertyFactory.newInteger("pageSize");
    public Property<Boolean> useQueryOption = PropertyFactory.newBoolean("useQueryOption");
    public Property<String> queryLiteralType = PropertyFactory.newString("queryLiteralType");
    public Property<String> queryOptionName = PropertyFactory.newString("queryOptionName");
    public Property<String> queryOptionLiterals = PropertyFactory.newString("queryOptionLiterals");

    public MarkLogicDataInputProperties(String name) {
        super(name);
    }

    @Override
    public MarkLogicDatasetProperties getDatasetProperties() {
        return dataset.getReference();
    }

    @Override
    public void setDatasetProperties(MarkLogicDatasetProperties datasetProperties) {
        dataset.setReference(datasetProperties);
    }

    @Override
    protected Set<PropertyPathConnector> getAllSchemaPropertiesConnectors(boolean isOutputConnection) {
        if (isOutputConnection) {
            return Collections.singleton(MAIN_CONNECTOR);
        } else {
            return Collections.emptySet();
        }
    }

}
