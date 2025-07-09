package com.suaistuds.monitoringeqiupment.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serial;


@Getter
public class SuaistudsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6593330219878485669L;

    private final HttpStatus status;
    private final String message;

    public SuaistudsException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public SuaistudsException(HttpStatus status, String message, Throwable exception) {
        super(exception);
        this.status = status;
        this.message = message;
    }

}