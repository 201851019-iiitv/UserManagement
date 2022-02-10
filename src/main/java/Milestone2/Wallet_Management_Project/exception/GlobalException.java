package Milestone2.Wallet_Management_Project.exception;


import Milestone2.Wallet_Management_Project.DTO.CustomReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

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




}
