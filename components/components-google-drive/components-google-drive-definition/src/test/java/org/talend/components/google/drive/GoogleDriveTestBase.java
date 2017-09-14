package org.talend.components.google.drive;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.avro.Schema;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.talend.components.api.container.DefaultComponentRuntimeContainerImpl;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.components.api.service.ComponentService;
import org.talend.components.api.service.common.DefinitionRegistry;
import org.talend.components.api.test.AbstractComponentTest2;
import org.talend.components.google.drive.connection.GoogleDriveConnectionProperties;
import org.talend.daikon.definition.service.DefinitionRegistryService;
import org.talend.daikon.properties.Properties;
import org.talend.daikon.properties.ValidationResult;
import org.talend.daikon.properties.ValidationResult.Result;
import org.talend.daikon.properties.service.Repository;
import org.talend.daikon.sandbox.SandboxedInstance;

public class GoogleDriveTestBase extends AbstractComponentTest2 {

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    private ComponentService componentService;

    @Inject
    DefinitionRegistry testComponentRegistry;

    protected RuntimeContainer adaptor;

    public GoogleDriveTestBase() {
        adaptor = new DefaultComponentRuntimeContainerImpl();
    }

    @Before
    public void initializeComponentRegistryAndService() {
        componentService = null;
    }

    @Override
    public DefinitionRegistryService getDefinitionRegistry() {
        if (testComponentRegistry == null) {
            testComponentRegistry = new DefinitionRegistry();
            testComponentRegistry.registerComponentFamilyDefinition(new GoogleDriveFamilyDefinition());
        }
        return testComponentRegistry;
    }

    public class SandboxedInstanceTestFixture implements AutoCloseable {

        SandboxedInstance sandboxedInstance;

        public GoogleDriveRuntime runtimeSourceOrSink;

        public void setUp() throws Exception {
            GoogleDriveComponentDefinition.SandboxedInstanceProvider sandboxedInstanceProvider = mock(GoogleDriveComponentDefinition.SandboxedInstanceProvider.class);
            GoogleDriveComponentDefinition.setSandboxedInstanceProvider(sandboxedInstanceProvider);
            sandboxedInstance = mock(SandboxedInstance.class);
            when(sandboxedInstanceProvider.getSandboxedInstance(anyString(), anyBoolean())).thenReturn(sandboxedInstance);
            runtimeSourceOrSink = mock(GoogleDriveRuntime.class, withSettings());
            doReturn(runtimeSourceOrSink).when(sandboxedInstance).getInstance();
            when(runtimeSourceOrSink.validateConnection(any(GoogleDriveConnectionProperties.class))).thenReturn(
                    new ValidationResult(Result.OK));
        }

        public void changeValidateConnectionResult(Result result) {
            when(runtimeSourceOrSink.validateConnection(any(GoogleDriveConnectionProperties.class))).thenReturn(
                    new ValidationResult(result));
        }

        public void tearDown() throws Exception {
            GoogleDriveComponentDefinition
                    .setSandboxedInstanceProvider(GoogleDriveComponentDefinition.SandboxedInstanceProvider.INSTANCE);
        }

        @Override
        public void close() throws Exception {
            tearDown();
        }
    }

    static class RepoProps {

        Properties props;

        String name;

        String repoLocation;

        Schema schema;

        String schemaPropertyName;

        RepoProps(Properties props, String name, String repoLocation, String schemaPropertyName) {
            this.props = props;
            this.name = name;
            this.repoLocation = repoLocation;
            this.schemaPropertyName = schemaPropertyName;
            if (schemaPropertyName != null) {
                this.schema = (Schema) props.getValuedProperty(schemaPropertyName).getValue();
            }
        }
    }

    class TestRepository implements Repository {

        private int locationNum;

        public String componentIdToCheck;

        public ComponentProperties properties;

        public List<RepoProps> repoProps;

        TestRepository(List<RepoProps> repoProps) {
            this.repoProps = repoProps;
        }

        @Override
        public String storeProperties(Properties properties, String name, String repositoryLocation, String schemaPropertyName) {
            RepoProps rp = new RepoProps(properties, name, repositoryLocation, schemaPropertyName);
            repoProps.add(rp);
            return repositoryLocation + ++locationNum;
        }
    }

    private final List<RepoProps> repoProps = new ArrayList<>();

    protected Repository repo = new TestRepository(repoProps);
}
