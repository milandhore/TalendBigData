package org.talend.components.google.drive;

import java.util.Arrays;
import java.util.List;

public class GoogleDriveMimeTypes {

    public static final String MIME_TYPE_FOLDER = "application/vnd.google-apps.folder";

    public static final String MIME_TYPE_GOOGLE_DOCUMENT = "application/vnd.google-apps.document";

    public static final String MIME_TYPE_GOOGLE_DRAWING = "application/vnd.google-apps.drawing";

    public static final String MIME_TYPE_GOOGLE_PRESENTATION = "application/vnd.google-apps.presentation";

    public static final String MIME_TYPE_GOOGLE_SPREADSHEET = "application/vnd.google-apps.spreadsheet";

    public static final List<String> GOOGLE_DRIVE_APPS = Arrays.asList(MIME_TYPE_GOOGLE_DOCUMENT, MIME_TYPE_GOOGLE_DRAWING,
            MIME_TYPE_GOOGLE_PRESENTATION, MIME_TYPE_GOOGLE_SPREADSHEET);

    public static final String MIME_TYPE_CSV = "text/csv";

    public static final String MIME_TYPE_CSV_EXT = ".csv";

    public static final String MIME_TYPE_CSV_TAB = "text/tab-separated-values";

    public static final String MIME_TYPE_CSV_TAB_EXT = ".csv";

    public static final String MIME_TYPE_EPUB = "application/epub+zip";

    public static final String MIME_TYPE_EPUB_EXT = ".zip";

    public static final String MIME_TYPE_EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static final String MIME_TYPE_EXCEL_EXT = ".xlsx";

    public static final String MIME_TYPE_HTML = "text/html";

    public static final String MIME_TYPE_HTML_EXT = ".html";

    public static final String MIME_TYPE_HTML_ZIPPED = "application/zip";

    public static final String MIME_TYPE_HTML_ZIPPED_EXT = ".zip";

    public static final String MIME_TYPE_JPG = "image/jpeg";

    public static final String MIME_TYPE_JPG_EXT = ".jpg";

    public static final String MIME_TYPE_OO_DOCUMENT = "application/vnd.oasis.opendocument.text";

    public static final String MIME_TYPE_OO_DOCUMENT_EXT = ".odt";

    public static final String MIME_TYPE_OO_PRESENTATION = "application/vnd.oasis.opendocument.presentation";

    public static final String MIME_TYPE_OO_PRESENTATION_EXT = ".odp";

    public static final String MIME_TYPE_OO_SPREADSHEET = "application/x-vnd.oasis.opendocument.spreadsheet";

    public static final String MIME_TYPE_OO_SPREADSHEET_EXT = ".ods";

    public static final String MIME_TYPE_OO_XSPREADSHEET = "application/x-vnd.oasis.opendocument.spreadsheet";

    public static final String MIME_TYPE_OO_XSPREADSHEET_EXT = ".ods";

    public static final String MIME_TYPE_PDF = "application/pdf";

    public static final String MIME_TYPE_PDF_EXT = ".pdf";

    public static final String MIME_TYPE_PNG = "image/png";

    public static final String MIME_TYPE_PNG_EXT = ".png";

    public static final String MIME_TYPE_POWERPOINT = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    public static final String MIME_TYPE_POWERPOINT_EXT = ".pptx";

    public static final String MIME_TYPE_RTF = "application/rtf";

    public static final String MIME_TYPE_RTF_EXT = ".rtf";

    public static final String MIME_TYPE_SVG = "image/svg+xml";

    public static final String MIME_TYPE_SVG_EXT = ".svg";

    public static final String MIME_TYPE_TEXT = "text/plain";

    public static final String MIME_TYPE_TEXT_EXT = ".txt";

    public static final String MIME_TYPE_WORD = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    public static final String MIME_TYPE_WORD_EXT = ".docx";

    public static final String MIME_TYPE_ZIP = "application/zip";

    public static final String MIME_TYPE_ZIP_EXT = ".zip";

    public enum MimeTypes {

        CSV(MIME_TYPE_CSV, MIME_TYPE_CSV_EXT),
        CSV_TAB(MIME_TYPE_CSV_TAB, MIME_TYPE_CSV_TAB_EXT),
        EPUB(MIME_TYPE_EPUB, MIME_TYPE_EPUB_EXT),
        EXCEL(MIME_TYPE_EXCEL, MIME_TYPE_EXCEL_EXT),
        HTML(MIME_TYPE_HTML, MIME_TYPE_HTML),
        HTML_ZIPPED(MIME_TYPE_HTML_ZIPPED, MIME_TYPE_HTML_ZIPPED_EXT),
        JPG(MIME_TYPE_JPG, MIME_TYPE_JPG_EXT),
        OO_DOCUMENT(MIME_TYPE_OO_DOCUMENT, MIME_TYPE_OO_DOCUMENT_EXT),
        OO_PRESENTATION(MIME_TYPE_OO_PRESENTATION, MIME_TYPE_OO_PRESENTATION_EXT),
        OO_SPREADSHEET(MIME_TYPE_OO_SPREADSHEET, MIME_TYPE_OO_SPREADSHEET_EXT),
        PDF(MIME_TYPE_PDF, MIME_TYPE_PDF_EXT),
        PNG(MIME_TYPE_PNG, MIME_TYPE_PNG_EXT),
        POWERPOINT(MIME_TYPE_POWERPOINT, MIME_TYPE_POWERPOINT_EXT),
        RTF(MIME_TYPE_RTF, MIME_TYPE_RTF_EXT),
        SVG(MIME_TYPE_SVG, MIME_TYPE_SVG_EXT),
        TEXT(MIME_TYPE_TEXT, MIME_TYPE_TEXT_EXT),
        WORD(MIME_TYPE_WORD, MIME_TYPE_WORD_EXT);

        private String mimeType;

        private String extension;

        MimeTypes(String mimeType, String extension) {
            this.mimeType = mimeType;
            this.extension = extension;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getExtension() {
            return extension;
        }

    }

}
