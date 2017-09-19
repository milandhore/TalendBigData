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
package org.talend.components.google.drive.delete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;

import org.junit.Before;
import org.junit.Test;

public class GoogleDriveDeletePropertiesTest {

    GoogleDriveDeleteProperties properties;

    @Before
    public void setUp() throws Exception {
        properties = new GoogleDriveDeleteProperties("test");
    }

    @Test
    public void testGetAllSchemaPropertiesConnectors() throws Exception {
        assertThat(properties.getAllSchemaPropertiesConnectors(false), hasSize(0));
        assertThat(properties.getAllSchemaPropertiesConnectors(true), contains(properties.MAIN_CONNECTOR));
    }

}
