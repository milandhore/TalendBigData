package org.talend.components.marklogic.runtime.input;

import org.junit.Test;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.container.RuntimeContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

public class MarkLogicInputWriteOperationTest {

    @Test
    public void testGetSink() {
        MarkLogicInputSink inputSink = new MarkLogicInputSink();
        MarkLogicInputWriteOperation inputWriteOperation = inputSink.createWriteOperation();

        assertEquals(inputSink, inputWriteOperation.getSink());
    }

    @Test
    public void testInitialize() {
        RuntimeContainer mockedContainer = mock(RuntimeContainer.class);
        MarkLogicInputWriteOperation inputWriteOperation = new MarkLogicInputWriteOperation(null, null);

        inputWriteOperation.initialize(mockedContainer);

        verifyZeroInteractions(mockedContainer);
    }

    @Test
    public void testCreateWriter() {
        MarkLogicInputWriteOperation inputWriteOperation = new MarkLogicInputWriteOperation(null, null);
        MarkLogicRowProcessor reader = inputWriteOperation.createWriter(null);

        assertNotNull(reader);
    }

    @Test
    public void testFinalize() {
        MarkLogicInputWriteOperation inputWriteOperation = new MarkLogicInputWriteOperation(null, null);
        List<Result> resultList = new ArrayList<>();
        Result r1 = new Result();
        Result r2 = new Result();
        r1.successCount = 2;
        r2.successCount = 1;

        r1.rejectCount = 4;
        r2.rejectCount = 1;

        r1.totalCount = 6;
        r2.totalCount = 2;

        resultList.add(r1);
        resultList.add(r2);
        Map<String, Object> finalResults = inputWriteOperation.finalize(resultList, null);

        assertTrue(finalResults.size() == 3);
        assertEquals(3, finalResults.get("successRecordCount"));
        assertEquals(5, finalResults.get("rejectRecordCount"));
        assertEquals(8, finalResults.get("totalRecordCount"));
    }
}
