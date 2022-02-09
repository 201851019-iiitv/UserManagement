package ambesh.UserManagement.utilities;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;


@Setter
@Getter
public class CustomReturnType {

    private String msg;
    private Timestamp timestamp;
    private HttpStatus status;



    public CustomReturnType(String msg, HttpStatus status){
          this.msg=msg;
          this.status=status;
          timestamp= new Timestamp(System.currentTimeMillis());
    }





}
