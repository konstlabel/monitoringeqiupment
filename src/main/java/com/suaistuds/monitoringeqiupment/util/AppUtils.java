package com.suaistuds.monitoringeqiupment.util;

import com.suaistuds.monitoringeqiupment.exception.SuaistudsException;
import org.springframework.http.HttpStatus;

public class AppUtils {
    public static final int MAX_PAGE_SIZE = 30;


    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new SuaistudsException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new SuaistudsException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new SuaistudsException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + MAX_PAGE_SIZE);
        }
    }
}
