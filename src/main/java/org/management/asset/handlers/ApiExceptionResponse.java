package org.management.asset.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

/**
 * @author HAYTHAM DAHRI
 * Exception response representation object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiExceptionResponse {

    /**
     * Exception response message property
     */
    private String message;

    /**
     * Exception throwable
     */
    private Throwable throwable;

    /**
     * Http Status
     */
    private HttpStatus httpStatus;

    /**
     * Http Status Code
     */
    private int statusCode;

    /**
     * Exception response time property
     */
    private ZonedDateTime timestamp;


}