package Milestone2.Wallet_Management_Project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException  extends RuntimeException{

    public BadRequestException(String msg){
        super(msg);

    }
}
