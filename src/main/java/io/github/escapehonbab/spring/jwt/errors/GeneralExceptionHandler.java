package io.github.escapehonbab.spring.jwt.errors;

import io.github.escapehonbab.spring.objects.ResponseBundle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseBundle handleNullPointerException() {
        return ResponseBundle.builder().response("An error occurred").responseCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }
}
