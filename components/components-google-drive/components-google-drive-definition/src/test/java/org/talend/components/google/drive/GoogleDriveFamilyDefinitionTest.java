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
