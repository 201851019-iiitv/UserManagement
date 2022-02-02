package Milestone2.Wallet_Management_Project.DTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.sql.Timestamp;


@Setter
@Getter
public class CustomReturnType {

    private String msg;
    private Timestamp timestamp;
    private HttpStatus status;



    public CustomReturnType(String msg, HttpStatus status){
          this.msg=msg;
          this.status=status;
          timestamp=new Timestamp(System.currentTimeMillis());
    }





}
