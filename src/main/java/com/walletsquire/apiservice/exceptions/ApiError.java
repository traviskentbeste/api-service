package com.walletsquire.apiservice.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Data
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String details;

    private ApiError() {
        // always set the time to now when this is created and we're in the central time zone
        timestamp = ZonedDateTime.now(ZoneId.of("America/Chicago"));
    }

    public ApiError(HttpStatus status, String message, String details) {
        this();
        this.status = status;
        this.message = message;
        this.details = details;
    }

}
