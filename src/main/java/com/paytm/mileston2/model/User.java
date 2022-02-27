package com.paytm.mileston2.model;

import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.utilities.validation.Validation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
// for user table id will his mobile number
public class User extends Validation {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long userId;

   @Column(name = "mobile_number", nullable = false, unique = true, updatable = false, length = 10)
    private  String mobileNumber;

    @Column(name = "username", nullable = false, unique = true, updatable = false)
    private  String username;

    @Column(name = "password", nullable = false)
    private  String password;


    @Column(name = "Email" ,nullable = false ,unique = true ,updatable = false)
    private String email;

    @Column(name = "Name" ,nullable = false)
    private String name;

    @Column(name = "createDate")
    private Date createDate;


    @Column(name = "isActiveUser")
    private boolean isActiveUser;

    @Column(name = "isActiveWallet")
    private boolean isActiveWallet;

    @Column(name = "address")
    private String address;



    @OneToOne(cascade = CascadeType.ALL)
    private Wallet  wallet;


    //create one extra column of walletId which is primary key of wallet table;

    public User(UserBuilder userBuilder){
        this.mobileNumber = userBuilder.mobileNumber;
        this.username= userBuilder.username;
        this.password= userBuilder.password;
        this.email= userBuilder.email;
        this.name= userBuilder.name;
        this.createDate=userBuilder.createDate;
        this.address= userBuilder.address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public boolean getIsActiveWallet() {
        return isActiveWallet;
    }

    public boolean getIsActiveUser() {
        return isActiveUser;
    }

    public String getAddress() {
        return address;
    }

    public Long getUserId() {
        return userId;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    // these two properties can be mutable.
    public void setIsActiveUser(boolean isActiveUser) {
        this.isActiveUser = isActiveUser;

    }
    public void setIsActiveWallet(boolean isActiveWallet) {
        this.isActiveWallet = isActiveWallet;

    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", mobile_number='" + mobileNumber + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", createDate=" + createDate +
                ", isActiveUser=" + isActiveUser +
                ", address='" + address + '\'' +
                ", isActiveWallet=" + isActiveWallet +
                ", wallet=" + wallet +
                '}';
    }

    //userBuilder class.
    public static class UserBuilder {

        private String mobileNumber;
        private String username;
        private String password;
        private String email;
        private String name;
        private Date createDate;
        private String address;

        public UserBuilder setMobileNumber(final String mobileNumber) {
            this.mobileNumber= mobileNumber;
            return this;
        }

        public UserBuilder setUsername(final String username) {
            this.username = username;
            return this;
        }
        public UserBuilder setPassword(final String password) {
            this.password = password;
             return  this;
        }

        public UserBuilder setAddress(final String address) {
            this.address = address;
            return  this;
        }
        public UserBuilder setEmail(final String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setName(final String name) {
            this.name = name;
            return this;
        }
        public UserBuilder setCreateDate(final Date createDate) {
            this.createDate= createDate;
            return this;
        }

        //1. email,username,password,name,mobile
        //2. check email & mobile formatting .
        public User build() {
            if (this.mobileNumber == null || this.email == null || this.username == null || this.password == null || this.name == null || !emailValidation(this.email) || !mobileNumberValidation(this.mobileNumber)) {
                throw new BadRequestException("All required attributes(email,username,password,name,mobile) are not present or email/mobile not in correct format .");
            }

            return new User(this);
        }
    }

    }
