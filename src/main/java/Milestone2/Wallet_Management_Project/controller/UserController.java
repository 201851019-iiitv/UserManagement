package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.returnPackage.returnMssg;
import Milestone2.Wallet_Management_Project.service.UserService;
import Milestone2.Wallet_Management_Project.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController

public class UserController extends Validation {

    @Autowired
    private UserService userService;


    @PostMapping("/user")
    public returnMssg createUser(@RequestBody User user) {
          //validate user mobile number
           if(!mobileNumberValidation(user.getMobileno()))
               throw new BadRequestException("Invalid mobile number !");
           if(!emailValidation(user.getEmail()))
               throw new BadRequestException("Invalid email number !");
            if(userService.findByMobileno(user.getMobileno())==null && userService.findByEmail(user.getEmail())==null ) {
               try {
                    user.setStatus("Active");
                    user.setCreateDate(new Date());
                    userService.createUser(user);
                   returnMssg mssg=new returnMssg("User created successfully !",HttpStatus.CREATED);
                    return mssg;
                }
               catch (Exception e){
                   throw  new BadRequestException("User can't be created !");
               }

            }

                throw new BadRequestException("User already exist!");
        }




    @GetMapping("/user")
    public  ResponseEntity<User> getUserById(@RequestParam long userId){
        // Done:
        User user =userService.getUserById(userId).orElseThrow(()-> new ResourceNotFoundException("User does not exist with this Id !"));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/user")
    public  void updateUser(User user){
        if(!mobileNumberValidation(user.getMobileno()))
            throw new BadRequestException("Invalid mobile number !");
        if(!emailValidation(user.getEmail()))
            throw new BadRequestException("Invalid email number !");
        try {
            userService.updateUser(user);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Updated");
        }
    }

    @DeleteMapping("/user")
    public void deleteUser(User user){

        try {
            userService.deleteUser(user);
//            user.setStatus("Inactive");
//            userService.updateUser(user);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Deleted");
        }

    }






}
