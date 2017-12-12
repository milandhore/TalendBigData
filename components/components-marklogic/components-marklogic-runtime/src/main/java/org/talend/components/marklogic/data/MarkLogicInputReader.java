package org.talend.components.marklogic.data;

import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.common.FixedConnectorsComponentProperties;
import org.talend.components.marklogic.runtime.input.MarkLogicCriteriaReader;

/**
 *
 *
 */
public class MarkLogicInputReader extends MarkLogicCriteriaReader {

    protected MarkLogicInputReader(BoundedSource source, RuntimeContainer container, MarkLogicDataInputProperties properties) {
        super(source, container, properties);
    }

    @Override
    protected Setting prepareSettings(FixedConnectorsComponentProperties inputProperties) {
        MarkLogicDataInputProperties properties = (MarkLogicDataInputProperties) inputProperties;

        return new Setting(
                properties.getDatasetProperties().main.schema.getValue(),
                properties.criteria.getValue(), 0, properties.pageSize.getValue(),
                properties.useQueryOption.getValue(), properties.queryLiteralType.getValue(),
                properties.queryOptionName.getValue(), properties.queryOptionLiterals.getValue(),
                properties.getDatasetProperties().getDatastoreProperties().isReferencedConnectionUsed());
    }

}
