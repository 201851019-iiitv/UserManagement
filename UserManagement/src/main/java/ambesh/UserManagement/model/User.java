package ambesh.UserManagement.model;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "mobileNumber")
    private String mobileNumber;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;
}