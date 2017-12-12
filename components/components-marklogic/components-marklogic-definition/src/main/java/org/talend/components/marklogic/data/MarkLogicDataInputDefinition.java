package org.talend.components.marklogic.data;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.talend.components.api.component.AbstractComponentDefinition;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.SupportedProduct;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.marklogic.RuntimeInfoProvider;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

/**
 *
 *
 */
public class MarkLogicDataInputDefinition extends AbstractComponentDefinition {

    public static final String NAME = "MarkLogicDataInput";

    public MarkLogicDataInputDefinition() {
        super(NAME, ExecutionEngine.DI);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return MarkLogicDataInputProperties.class;
    }

    @Override
    public Property<?>[] getReturnProperties() {
        return new Property[0];
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties,
            ConnectorTopology connectorTopology) {
        assertEngineCompatibility(engine);
        return RuntimeInfoProvider.getCommonRuntimeInfo("org.talend.components.marklogic.data.MarkLogicDataSource");
    }

    @Override
    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.OUTGOING);
    }

    @Override
    public List<String> getSupportedProducts() {
        return Arrays.asList(SupportedProduct.DATAPREP, SupportedProduct.DATASTREAMS);
    }

}
