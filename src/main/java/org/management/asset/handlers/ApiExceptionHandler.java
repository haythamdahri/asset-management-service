package org.management.asset.handlers;

import org.management.asset.exceptions.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.InputMismatchException;

/**
 * @author HAYTHAM DAHRI
 * Entity excpetion handler class
 */
@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * BusinessException handler method
     */
    @ExceptionHandler(value = {BusinessException.class, IllegalArgumentException.class, InputMismatchException.class})
    public ResponseEntity<?> handleBusinessExceptions(Exception ex) {
        // Create Payload
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse();
        apiExceptionResponse.setMessage(ex.getMessage());
        apiExceptionResponse.setThrowable(ex);
        apiExceptionResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
        apiExceptionResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        apiExceptionResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Z")));
        // return response
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiExceptionResponse);
    }

    /**
     * BusinessException handler method
     */
    @ExceptionHandler(value = {SQLException.class, HttpServerErrorException.InternalServerError.class, NullPointerException.class})
    public ResponseEntity<?> handleInternalErrorException(Exception ex) {
        // Create Payload
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse();
        apiExceptionResponse.setMessage(ex.getMessage());
        apiExceptionResponse.setThrowable(ex);
        apiExceptionResponse.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        apiExceptionResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        apiExceptionResponse.setTimestamp(ZonedDateTime.now(ZoneId.of("Z")));
        // return response
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiExceptionResponse);
    }
}
