package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.Validation.validity;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController extends validity {

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
            if(!doesExist(user)) {
                user.setStatus("Active");
                user.setCreateDate(new Date());
                userService.createUser(user);

                return new ResponseEntity<>("User created successfully !", HttpStatus.CREATED);

            }

                return new ResponseEntity<>("User already exist!",HttpStatus.CONFLICT);
        }




    @GetMapping
    public  ResponseEntity<?> getUserById(@RequestParam long userId){
        // Todo:
        Optional<User> user =userService.getUserById(userId);
        return new ResponseEntity<>(ResponseEntity.ok(user),HttpStatus.ACCEPTED);
    }

    @PutMapping
    public  void updateUser(User user){
        userService.updateUser(user);
    }

    @DeleteMapping
    public void deleteUser(User user){
        userService.deleteUser(user);

    }






}
