package com.paytm.mileston2.DAO.Impl;

import com.paytm.mileston2.model.User;

import java.util.List;

public interface IUserDao {

    List<User> findAllUsers();                              // to get all users
    User saveUser(User user);                             // to save the user in database.
    User findUserById(Long userId);                      //to get user by his/her userid.
    void deleteUser(Long userId);                      //to delete a particular user by his/her userid.
    User findByMobileNumber(String mobileNumber);         // to get a user by his/her mobile number.
    User findByEmail(String email);                 // to get a user by his/her email.
    User findByUsername(String username);         // to get a user by his/her username.

}
