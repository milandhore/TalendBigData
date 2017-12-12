package org.talend.components.marklogic.exceptions;

import org.talend.daikon.exception.error.ErrorCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MarkLogicErrorCode implements ErrorCode {


    public static final String PRODUCT_TALEND_COMPONENTS = "TCOMP";

    public static final String GROUP_COMPONENT_MARKLOGIC = "MARKLOGIC";

    private String code;
    private int httpStatus;
    List<String> expectedContextEntries;

    public MarkLogicErrorCode(String code) {
        this(code, 500, Collections.<String>emptyList());
    }

    public MarkLogicErrorCode(String code, String... contextEntries) {
        this(code, 500, Arrays.asList(contextEntries));
    }

    public MarkLogicErrorCode(String code, int httpStatusCode, String... contextEntries) {
        this(code, httpStatusCode, Arrays.asList(contextEntries));
    }

    public MarkLogicErrorCode(String code, int httpStatus, List<String> contextEntries) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.expectedContextEntries = contextEntries;
    }

    @Override
    public String getProduct() {
        return PRODUCT_TALEND_COMPONENTS;
    }

    @Override
    public String getGroup() {
        return GROUP_COMPONENT_MARKLOGIC;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public Collection<String> getExpectedContextEntries() {
        return expectedContextEntries;
    }

    @Override
    public String getCode() {
        return code;
    }
}
