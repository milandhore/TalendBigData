package org.talend.components.marklogic.data;

import org.apache.avro.Schema;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.talend.components.common.ComponentConstants;
import org.talend.components.marklogic.tmarklogicconnection.MarkLogicConnectionProperties;
import org.talend.daikon.properties.presentation.Form;

public class MarkLogicDataInputPropertiesTest {

    MarkLogicDataInputProperties properties;

    MarkLogicDatasetProperties dataset = new MarkLogicDatasetProperties("dataset");

    @Before
    public void setup() {
        properties = new MarkLogicDataInputProperties("dataInput");
        properties.setDatasetProperties(dataset);
    }

    @Test
    public void testSetupProperties() {
        Assert.assertFalse(properties.useQueryOption.getValue());
        Assert.assertNull(properties.pageSize.getValue());
        Assert.assertNull(properties.queryLiteralType.getValue());
        Assert.assertNull(properties.queryOptionLiterals.getValue());
        Assert.assertNull(properties.getDatasetProperties().main.schema.getValue());

        properties.setupProperties();

        Assert.assertFalse(properties.useQueryOption.getValue());
        Assert.assertNotNull(properties.pageSize.getValue());
        Assert.assertEquals("XML", properties.queryLiteralType.getValue());
        Assert.assertEquals(" ", properties.queryOptionLiterals.getTaggedValue(ComponentConstants.LINE_SEPARATOR_REPLACED_TO));
        Schema schema = properties.getDatasetProperties().main.schema.getValue();
        Assert.assertNotNull(schema);
        Assert.assertEquals("docId", schema.getFields().get(0).name());
        Assert.assertEquals("docContent", schema.getFields().get(1).name());
    }

    @Test
    public void testSetupLayout() {
        MarkLogicConnectionProperties datastore = new MarkLogicConnectionProperties("datastore");
        datastore.init();
        dataset.setDatastoreProperties(datastore);
        dataset.init();

        Assert.assertNull(properties.getForm(Form.MAIN));

        properties.setupLayout();

        Form main = properties.getForm(Form.MAIN);
        Assert.assertNotNull(main);
        Assert.assertNotNull(main.getWidget(properties.dataset.getReference()));
        Assert.assertNotNull(main.getWidget(properties.dataset.getReference().getDatastoreProperties()));
        Assert.assertNotNull(main.getWidget(properties.criteria));
        Assert.assertNotNull(main.getWidget(properties.pageSize));
        Assert.assertNotNull(main.getWidget(properties.useQueryOption));
        Assert.assertNotNull(main.getWidget(properties.queryLiteralType));
        Assert.assertNotNull(main.getWidget(properties.queryOptionName));
        Assert.assertNotNull(main.getWidget(properties.queryOptionLiterals));
    }

    @Test
    public void testRefreshLayout() {
        MarkLogicConnectionProperties datastore = new MarkLogicConnectionProperties("datastore");
        datastore.init();
        dataset.setDatastoreProperties(datastore);
        dataset.init();

        properties.init();
        Form main = properties.getForm(Form.MAIN);
        Assert.assertFalse(main.getWidget(properties.queryLiteralType).isVisible());
        Assert.assertFalse(main.getWidget(properties.queryOptionName).isVisible());
        Assert.assertFalse(main.getWidget(properties.queryOptionLiterals).isVisible());

        properties.useQueryOption.setValue(true);

        properties.refreshLayout(main);

        Assert.assertTrue(main.getWidget(properties.queryLiteralType).isVisible());
        Assert.assertTrue(main.getWidget(properties.queryOptionName).isVisible());
        Assert.assertTrue(main.getWidget(properties.queryOptionLiterals).isVisible());
    }

    @Test
    public void testGetDatasetProperties() {
        Assert.assertEquals(dataset, properties.getDatasetProperties());
    }

    @Test
    public void testGetAllSchemaPropertiesConnectors() {
        Assert.assertTrue(properties.getAllSchemaPropertiesConnectors(false).isEmpty());
        Assert.assertEquals(1, properties.getAllSchemaPropertiesConnectors(true).size());
        Assert.assertThat(properties.getAllSchemaPropertiesConnectors(true), Matchers.contains(properties.MAIN_CONNECTOR));
    }
}
