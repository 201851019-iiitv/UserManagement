package Milestone2.Wallet_Management_Project.returnPackage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;


@Setter
@Getter
public class returnMssg {

    private String msg;
    private Timestamp timestamp;
    private HttpStatus status;



    public returnMssg(String msg, HttpStatus status){
          this.msg=msg;
          this.status=status;
          timestamp=new Timestamp(System.currentTimeMillis());
    }





}
