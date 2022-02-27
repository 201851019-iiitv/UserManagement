package com.paytm.mileston2.controller;


import com.paytm.mileston2.DTO.ReqUser;
import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController{

    // create Logger
    private static Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    
    @PostMapping("/user")
    public CustomReturnType createUser(@RequestBody User Requser) {

        try{
             return userService.createUser(Requser);
        }
        catch(Exception e){
            logger.info("user is not created with user : "+Requser);
            throw  new BadRequestException("can't be created !");
        }

    }

    @GetMapping("/user")
    public  User getUserById(@RequestParam long userId){
        // Done:
        User user =userService.getUserById(userId);
        logger.info("user retrieved by userId "+userId);
        return user;
    }

    @PutMapping("/user")
    public  CustomReturnType updateUser(User user){
        try {

           return userService.updateUser(user);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Updated");
        }

    }

    @DeleteMapping("/user")
    public CustomReturnType deleteUser(@RequestParam Long userId){
        try {
           return  userService.deleteUser(userId);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Deleted");
        }
    }






}
