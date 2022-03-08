package com.paytm.mileston2.controller;


import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
public class UserController{

    // create Logger
    private static Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;
    
    @PostMapping("/user")
    @Operation(description = "This method use to create new user .",summary="to create new user .JWT Token is not required")
    public CustomReturnType createUser(@Valid @RequestBody User Requser) {
        try{
             return userService.createUser(Requser);
        }
        catch(Exception e){
            logger.info("user is not created with user : "+Requser);
            throw  new BadRequestException("can't be created !");
        }

    }

    @GetMapping("/user")
    @Operation(summary= "This method use to get user by user Id .",description="JWT Token is not required")
    public  User getUserById(@RequestParam long userId){
        // Done:
        User user =userService.getUserById(userId);
        logger.info("user retrieved by userId "+userId);
        return user;
    }

    @PutMapping("/user")
    @Operation(summary = "This method use to update a existing user .",description="to update existing user . Required JWT Token",security = @SecurityRequirement(name = "bearerAuth"))
    public  CustomReturnType updateUser(@Valid User user){
        try {

           return userService.updateUser(user);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Updated" +e.getLocalizedMessage());
        }

    }

    @DeleteMapping("/user")
    @Operation(summary = "This method use to delete existing user .",description="to delete exist user .Required JWT Token",security = @SecurityRequirement(name = "bearerAuth"))
    public CustomReturnType deleteUser(@RequestParam Long userId){
        try {
           return  userService.deleteUser(userId);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Deleted");
        }
    }






}
