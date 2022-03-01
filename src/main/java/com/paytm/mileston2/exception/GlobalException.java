package com.paytm.mileston2.exception;


import com.paytm.mileston2.DTO.CustomReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomReturnType> ResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){

        CustomReturnType msg=new CustomReturnType(resourceNotFoundException.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(msg);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomReturnType> BadRequest(BadRequestException badRequestException){
        CustomReturnType msg=new CustomReturnType(badRequestException.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(msg);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
