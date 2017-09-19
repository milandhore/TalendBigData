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
package org.talend.components.google.drive.list;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.talend.components.google.drive.list.GoogleDriveListProperties.ListMode;

public class GoogleDriveListPropertiesTest {

    GoogleDriveListProperties properties;

    @Before
    public void setUp() throws Exception {
        properties = new GoogleDriveListProperties("test");
    }

    @Test
    public void testListMode() throws Exception {
        assertEquals("Files", ListMode.Files.name());
        assertEquals(ListMode.Files, ListMode.valueOf("Files"));
        assertEquals("Directories", ListMode.Directories.name());
        assertEquals(ListMode.Directories, ListMode.valueOf("Directories"));
        assertEquals("Both", ListMode.Both.name());
        assertEquals(ListMode.Both, ListMode.valueOf("Both"));
    }
}
