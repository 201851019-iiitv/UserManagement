package Milestone2.Wallet_Management_Project.controller;
import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {

            if(userService.findByMobileno(user.getMobileno())==null && userService.findByEmail(user.getEmail())==null ) {
               try {
                    user.setStatus("Active");
                    user.setCreateDate(new Date());
                    userService.createUser(user);

                    return new ResponseEntity<>("User created successfully !", HttpStatus.CREATED);
                }
               catch (Exception e){
                   throw  new BadRequestException("User can't be created !");
               }

            }

                return new ResponseEntity<>("User already exist!",HttpStatus.CONFLICT);
        }




    @GetMapping
    public  ResponseEntity<User> getUserById(@RequestParam long userId){
        // Done:
        User user =userService.getUserById(userId).orElseThrow(()-> new ResourceNotFoundException("User does not exist with this Id !"));
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public  void updateUser(User user){
        try {
            userService.updateUser(user);
        }
        catch (Exception e){
            throw new BadRequestException("User can't be Updated");
        }
    }

    @DeleteMapping
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
