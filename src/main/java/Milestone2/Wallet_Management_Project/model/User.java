package Milestone2.Wallet_Management_Project.model;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user")
// for user table id will his mobile number
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(name = "mobileNo", nullable = false, unique = true, updatable = false, length = 10)
    private  String mobileno;

    @Column(name = "userName", nullable = false, unique = true, updatable = false)
    private  String username;

    @Column(name = "password", nullable = false)
    private  String password;


    @Column(name = "Email" ,nullable = false ,unique = true ,updatable = false)
    private String email;

    @Column(name = "Name" ,nullable = false)
    private String name;

    @Column(name = "createdDate")
    private Date createDate;


    @Column(name = "Status")
    private String status;

    @Column(name = "Address")
    private String address;

    @OneToOne
    private Wallet  wallet;
 //create one extra column of walletId which is primary key of wallet tabel;

}
