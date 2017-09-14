package org.talend.components.google.drive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.api.ComponentFamilyDefinition;
import org.talend.components.api.ComponentInstaller;
import org.talend.components.api.wizard.ComponentWizardDefinition;
import org.talend.daikon.definition.Definition;

public class GoogleDriveFamilyDefinitionTest extends GoogleDriveTestBase {

    GoogleDriveFamilyDefinition def;

    @Before
    public void setUp() throws Exception {
        def = new GoogleDriveFamilyDefinition();
    }

    @Test
    public void testGoogleDriveFamilyDefinition() {
        assertNotNull(getDefinitionRegistry());
        assertEquals("Google Drive", new GoogleDriveFamilyDefinition().getName());
        assertEquals(7, testComponentRegistry.getDefinitions().size());
    }

    @Test
    public void testInstall() throws Exception {
        final GoogleDriveFamilyDefinition def = new GoogleDriveFamilyDefinition();
        ComponentInstaller.ComponentFrameworkContext ctx = new ComponentInstaller.ComponentFrameworkContext() {

            @Override
            public void registerComponentFamilyDefinition(ComponentFamilyDefinition def) {
                assertEquals("Google Drive", def.getName());
            }

            @Override
            public void registerDefinition(Iterable<? extends Definition> defs) {
                assertNull(defs);
            }

            @Override
            public void registerComponentWizardDefinition(Iterable<? extends ComponentWizardDefinition> defs) {
                assertNull(def);
            }
        };
        def.install(ctx);
    }

}
