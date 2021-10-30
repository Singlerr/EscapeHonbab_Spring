package io.github.escapehonbab.spring.jwt.errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = InvalidTypeIdException.class)
    public ResponseBundle handleJacksonException(){
        return ResponseBundle.builder().response("An error occurred").responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
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
}
