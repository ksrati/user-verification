package com.user.verification.CustomException;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@ControllerAdvice
@RestController
public class ErrorController {
	  @ExceptionHandler(CustomValidationException.class)
    protected ResponseEntity<Object> handleCustomValidationException(CustomValidationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        
        //object of custom error 
        CustomError error = new CustomError(status.value(), ex.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> handleException(Exception ex) {
        CustomError error = new CustomError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @GetMapping("/error")
    public ResponseEntity<CustomError> getError() {
        CustomError error = new CustomError(HttpStatus.NOT_FOUND.value(), "Page Not Found", LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
