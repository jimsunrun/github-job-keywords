/*
 * Copyright 2015 Jim Alexander, Aesthetic Software, Inc. (jhaood@gmail.com) Apache Version 2
 * license: http://www.apache.org/licenses/LICENSE-2.0
 */
package com.aestheticsw.jobkeywords.service.termextractor.impl.indeed;

/**
 * A checked exception to handle invalid query expressions differently from IOExceptions or other
 * severe exceptions.
 */
public class IndeedQueryException extends Exception {

    private static final long serialVersionUID = -3312431007385262417L;

    public IndeedQueryException() {
        super();
    }

    public IndeedQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public IndeedQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndeedQueryException(String message) {
        super(message);
    }

    public IndeedQueryException(Throwable cause) {
        super(cause);
    }

}
