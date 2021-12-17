package io.github.escapehonbab.spring.jwt.errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseBundle handleIllegalArgumentException() {
        return ResponseBundle.builder().response(HttpStatus.NOT_FOUND.getReasonPhrase()).responseCode(HttpStatus.NOT_FOUND.value()).build();
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseBundle handleUnauthorisedException() {

        return ResponseBundle.builder().response(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseCode(HttpStatus.UNAUTHORIZED.value()).build();
    }
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseBundle handleAuthenticationException() {
        return ResponseBundle.builder().response(HttpStatus.UNAUTHORIZED.getReasonPhrase()).responseCode(HttpStatus.UNAUTHORIZED.value()).build();
    }
    @ExceptionHandler(value = InvalidTypeIdException.class)
    public ResponseBundle handleJacksonException() {
        return ResponseBundle.builder().response("An error occurred").responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseBundle handleIOException() {
        return ResponseBundle.builder().response(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseBundle handleNullPointerException() {
        return ResponseBundle.builder().response("An error occurred").responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseBundle handleHMNRE() {
        return ResponseBundle.builder().response("Required request body is missing: ").responseCode(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseBundle handleJSONError() {
        return ResponseBundle.builder().response(String.format("A field 'message' requires a specific format")).responseCode(HttpStatus.BAD_REQUEST.value()).build();
    }

    @ExceptionHandler(value = ImageNotFoundException.class)
    public ResponseBundle handleImageNotFoundException() {
        return ResponseBundle.builder().response("No image found").responseCode(HttpStatus.NOT_FOUND.value()).build();
    }
}
