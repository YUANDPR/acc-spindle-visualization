package com.acc;

import com.acc.core.exception.InvalidSignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleInvalidSignatureException(InvalidSignatureException ex) {
        return ex.getMessage();
    }

}
