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

import org.junit.Test;
import org.talend.components.google.drive.GoogleDriveMimeTypes.MimeTypes;

public class GoogleDriveMimeTypesTest {

    @Test
    public void testMimeTypesGetMimeType() throws Exception {
        assertEquals(GoogleDriveMimeTypes.MIME_TYPE_CSV, MimeTypes.CSV.getMimeType());
    }

    @Test
    public void testMimeTypesGetExtension() throws Exception {
        assertEquals(GoogleDriveMimeTypes.MIME_TYPE_CSV_EXT, MimeTypes.CSV.getExtension());
    }
}
