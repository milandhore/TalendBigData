package org.talend.components.marklogic.exceptions;

import org.talend.components.api.exception.ComponentException;
import org.talend.daikon.exception.error.ErrorCode;

public class MarkLogicException extends ComponentException {

    public MarkLogicException(ErrorCode code) {
        super(code);
    }

    public MarkLogicException(ErrorCode code, Throwable cause) {
        super(code, cause);
    }
}
