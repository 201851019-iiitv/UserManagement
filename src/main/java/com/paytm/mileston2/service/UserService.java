package com.paytm.mileston2.service;

import com.paytm.mileston2.DAO.UserDao;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.User;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    final static Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserDao userDao;


    @Transactional
    public List<User> getAllUser(){

        return userDao.findAllUsers();
    }
    @Transactional
    public CustomReturnType createUser(User user){
        // build the user first and validate.
        User user1 = new User.UserBuilder()
                .setName(user.getName())
                .setMobileNumber(user.getMobileNumber())
                .setAddress(user.getAddress())
                .setEmail(user.getEmail())
                .setPassword(user.getPassword())
                .setUsername(user.getUsername())
                .setCreateDate(new Date())
                .build();
     logger.debug("create : " +user1);
        //these properties are mutable
        user1.setIsActiveUser(true);
        user1.setIsActiveWallet(false);
        //Done:check user1 already exist ? in database .
        if(userDao.findByMobileNumber(user1.getMobileNumber())!=null || userDao.findByEmail(user1.getEmail())!=null || userDao.findByUsername(user1.getUsername())!=null) {
            logger.warn("user already exist with this mobile number/email/username "+user1.getMobileNumber() +" "+ user1.getEmail());
            return  new CustomReturnType("User already exist!",HttpStatus.BAD_REQUEST);
        }
            try {
                 userDao.saveUser(user1);
                logger.info("created user  " + user1);
                return new CustomReturnType("User created successfully.", HttpStatus.CREATED);

            } catch (Exception e) {
                throw new BadRequestException("User can't be created !. userDetails :"+ user1);
            }
        }

    @Transactional
    public User getUserById(Long userId){
        logger.info("retrieved user by userId :" + userId);
        return userDao.findUserById(userId);
    }

    @Transactional
    public  CustomReturnType updateUser(User user1){
            try {
                userDao.saveUser(user1);
                logger.info("updated user : " + user1);
                return new CustomReturnType("User updated successfully.", HttpStatus.ACCEPTED);

            } catch (Exception e) {
                throw new RuntimeException("User can't be update !. because user doesn't exist with : "+ user1);
            }

    }

    @Transactional
    public CustomReturnType deleteUser(Long userId){
        userDao.deleteUser(userId);
        logger.info("user delete with userId : " +userId);
        return new CustomReturnType("User delete successfully" ,HttpStatus.ACCEPTED);
    }


   @Transactional
    public User findByMobileNumber(String mobileNumber) {
       logger.info("retrieved user by mobileNumber: " + mobileNumber);
       return userDao.findByMobileNumber(mobileNumber);
    }

    @Transactional
    public User findByEmail(String email) {
        logger.debug("retrieved user by email: " + email);
        return userDao.findByEmail(email);
    }


    @Transactional

    public  User findUserByUsername(String username){
        logger.debug("retrieved user by username: " + username);
        return userDao.findByUsername(username);
    }


    @Transactional
    public User deleteUserByMobileNumber(String mobileNumber) {
        User user= userDao.findByMobileNumber(mobileNumber);
        userDao.deleteUser(user.getUserId());
        logger.debug("delete user by mobileNumber :" + mobileNumber + " & userId : "+ user.getUserId());
        return  user;
    }
}
