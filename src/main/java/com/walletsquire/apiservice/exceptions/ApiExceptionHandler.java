package com.walletsquire.apiservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    /*
       Example:
       Entity with id=1 is the only one existing in the database and we look for id=2
    */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(EntityNotFoundException ex) {

        //List<String> details = new ArrayList<>();
        //details.add(ex.getLocalizedMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Resource Not Found",
                ex.getMessage()
        );

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        Create missing semicolon between two fields
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Malformed JSON request" ,
                ex.getMessage());

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        A required field in the entity is empty.  But it must be marked in the entity to throw this error
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining("\n"));

        System.out.println(ex.getBindingResult().getFieldErrors().toString());
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Input Fields Validation Error(s)",
                details);

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        They put a string instead of an integer in the get field
        ex: /api/v1/<uri>/test when it should be /api/v1/<uri>>/1
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Type Mismatch" ,
                ex.getMessage());

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        They put a 0 when the min is 1.  This is set in the controller
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(Exception ex, WebRequest request) {

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Constraint Violations" ,
                ex.getMessage());

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        This handles two conditions

        1.  They attempt a PATCH, but it is not implemented
        2.  They try a get missing arguments it gets handled here
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        if (((ServletWebRequest)request).getRequest().getRequestURI().endsWith("/")) {

            ApiError apiError = new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "Invalid Request",
                    "Your request is invalid, please check arguments and try again.");

            return ResponseEntityBuilder.build(apiError);

        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Method Unsupported",
                "Method '" + ex.getMethod() + "' is unsupported");

        return ResponseEntityBuilder.build(apiError);
    }


    /*
        Example:
        They attempt with text instead of json, this is set in controller.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        ApiError apiError = new ApiError(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Unsupported Media Type" ,
                builder.toString());

        return ResponseEntityBuilder.build(apiError);

    }

    /*
        Example:
        There is an endpoint /api/v1/<uri>> and they try /api/v1/get-<uri>.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiError err = new ApiError(
                HttpStatus.NOT_FOUND,
                "Method Not Found" ,
                String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL())
        );

        return ResponseEntityBuilder.build(err);

    }

    /*
        Example:
        Catch all
     */
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        System.out.println("ex      : " + ex);
        System.out.println("request : " + request);
        ApiError err = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Error occurred" ,
                ex.getLocalizedMessage());

        return ResponseEntityBuilder.build(err);

    }

}
