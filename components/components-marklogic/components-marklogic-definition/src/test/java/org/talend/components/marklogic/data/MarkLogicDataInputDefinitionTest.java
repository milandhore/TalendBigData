package org.talend.components.marklogic.data;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.component.ConnectorTopology;
import org.talend.components.api.component.SupportedProduct;
import org.talend.components.api.component.runtime.ExecutionEngine;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.runtime.RuntimeInfo;

public class MarkLogicDataInputDefinitionTest {

    private MarkLogicDataInputDefinition definition;

    @Before
    public void setup() {
        definition = new MarkLogicDataInputDefinition();
    }

    @Test
    public void testGetPropertyClass() {
        Assert.assertEquals(MarkLogicDataInputProperties.class, definition.getPropertiesClass());
    }

    @Test
    public void testGetReturnProperties() {
        Assert.assertArrayEquals(new Property<?>[0], definition.getReturnProperties());
    }

    @Test
    public void testGetRuntimeInfo() {
        RuntimeInfo runtime = definition.getRuntimeInfo(ExecutionEngine.DI, null, ConnectorTopology.OUTGOING);
        Assert.assertEquals(MarkLogicDataInputDefinition.DATA_SOURCE, runtime.getRuntimeClassName());
    }

    @Test
    public void testGetSupportedProducts() {
        Assert.assertThat(definition.getSupportedProducts(), Matchers.contains(SupportedProduct.DATAPREP, SupportedProduct.DATASTREAMS));
    }
}
