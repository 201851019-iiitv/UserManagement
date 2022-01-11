package ambesh.UserManagement.model;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;


    @Column(name = "user_name")
    private String userName;


    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(name = "mobile_number")
    private String mobileNumber;


    @Column(name = "email_id")
    private String emailID;


    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    public User() {
    }

    public Long getUserId() {
        return user_id;
    }

    public void setUserId(Long userId) {
        this.user_id = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String validity(){
          if(userName==null )
              return "UserName";
          else if(firstName==null && lastName==null )
              return "first name & last name";
          else if(mobileNumber==null || mobileNumber.length()!=10)
              return "Mobile number";
          else if(emailID==null || !emailID.contains("@"))
              return "email Id";
          else if(address1==null && address2==null)
                return "Address";
          else
              return "completed";

    }
}