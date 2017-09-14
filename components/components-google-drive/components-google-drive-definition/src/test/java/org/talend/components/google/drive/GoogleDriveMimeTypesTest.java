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
