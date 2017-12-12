package org.talend.components.marklogic.runtime.input;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import org.apache.avro.Schema;
import org.junit.Test;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.marklogic.tmarklogicinput.MarkLogicInputProperties;
import org.talend.daikon.avro.AvroUtils;

import java.io.IOException;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarkLogicCriteriaReaderTest {

    @Test
    public void testStartWithoutConnection() throws IOException {
        MarkLogicSource mockedSource = mock(MarkLogicSource.class);
        when(mockedSource.connect(any(RuntimeContainer.class))).thenReturn(null);
        MarkLogicInputProperties properties = new MarkLogicInputProperties("inputProperties");
        properties.init();
        MarkLogicCriteriaReader criteriaReader = new MarkLogicCriteriaReader(mockedSource, null, properties);

        assertFalse(criteriaReader.start());
    }

    @Test
    public void testStartWithoutDocContentField() throws IOException {
        StringQueryDefinition mockedStringQueryDefinition = mock(StringQueryDefinition.class);
        QueryManager mockedQueryManager = mock(QueryManager.class);
        when(mockedQueryManager.newStringDefinition()).thenReturn(mockedStringQueryDefinition);
        when(mockedQueryManager.newStringDefinition(anyString())).thenReturn(mockedStringQueryDefinition);
        DatabaseClient mockedClient = mock(DatabaseClient.class);
        when(mockedClient.newDocumentManager()).thenReturn(null);
        when(mockedClient.newQueryManager()).thenReturn(mockedQueryManager);
        MarkLogicSource mockedSource = mock(MarkLogicSource.class);
        when(mockedSource.connect(any(RuntimeContainer.class))).thenReturn(mockedClient);

        MarkLogicInputProperties properties = new MarkLogicInputProperties("inputProperties");
        properties.init();
        Schema.Field docIdField = new Schema.Field("docId", AvroUtils._string(), null, (Object) null, Schema.Field.Order.IGNORE);
        properties.outputSchema.schema.setValue(Schema.createRecord("docIdOnlySchema", null, null, false,
                Collections.singletonList(docIdField)));
        MarkLogicCriteriaReader criteriaReader = new MarkLogicCriteriaReader(mockedSource, null, properties);

        System.out.println(criteriaReader.start());

    }
}
