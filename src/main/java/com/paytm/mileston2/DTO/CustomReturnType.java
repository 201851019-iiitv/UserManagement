package com.paytm.mileston2.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.sql.Timestamp;
import java.time.Instant;


@Setter
@Getter
public class CustomReturnType {

    private String msg;
    private Timestamp timestamp;
    private HttpStatus status;


    public CustomReturnType(String msg, HttpStatus status){
          this.msg=msg;
          this.status=status;
          timestamp= Timestamp.from(Instant.now());
    }





}
