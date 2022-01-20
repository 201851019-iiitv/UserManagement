package Milestone2.Wallet_Management_Project.exception;


import Milestone2.Wallet_Management_Project.returnPackage.returnMssg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalEception {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<returnMssg> ResourceNotFoundException(ResourceNotFoundException resourceNotFoundException){

        returnMssg msg=new returnMssg(resourceNotFoundException.getLocalizedMessage(), HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(msg);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<returnMssg> BadRequest(BadRequestException badRequestException){
        returnMssg msg=new returnMssg(badRequestException.getLocalizedMessage(),HttpStatus.BAD_REQUEST);
        return ResponseEntity.ok(msg);
    }
}
