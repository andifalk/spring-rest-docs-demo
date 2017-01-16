/*
 * *************************************************************************
 *
 * Copyright:       Robert Bosch GmbH, 2016
 *
 * *************************************************************************
 */

package com.example.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Error controller handling all kinds of errors.
 */
@ControllerAdvice
public class ErrorController {

    private static final String BAD_REQUEST_MSG = "Unreadable message or invalid arguments for API call";

    private static final String UNSUPPORTED_MEDIA_TYPE_MSG = "Unsupported media type";

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorController.class);

    /**
     * Error handler for invalid arguments, not readable messages or constraint violations specified in REST api.
     *
     * @param ex exception
     * @return error message
     */
    @ExceptionHandler({IllegalArgumentException.class, HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleAllBadRequestExceptions(Exception ex) {
        LOGGER.warn(BAD_REQUEST_MSG, ex);
        return ResponseEntity.badRequest().build();
    }

    /**
     * Error handler for unsupported media types in REST api.
     *
     * @param ex exception
     * @return error message
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<?> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        LOGGER.error(UNSUPPORTED_MEDIA_TYPE_MSG, ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
    }

    /**
     * Error handler for access denied (403) errors in REST api.
     *
     * @param ex exception
     * @return error message
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(Exception ex) {
        LOGGER.error("Access denied for API call", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    /**
     * Error handler for general errors in REST api.
     *
     * @param ex exception
     * @return error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        LOGGER.error("Internal error for API call", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     * Error handler for optimistic locking errors.
     *
     * @param ex the exception containing information about the violation
     * @return an error resource
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> handleOptimisticLockingException(
            OptimisticLockingFailureException ex) {

        LOGGER.warn(String.format("Precondition is not satisfied: %s", ex.getMessage()));
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
