package ambesh.UserManagement.controller;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import ambesh.UserManagement.model.User;
import ambesh.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ToDo: handle validation(unique id,username,mobileno,email)
    @PostMapping
    public User createUser(@RequestBody User user) {
        return (User) userRepository.save(user);
    }


    @GetMapping("{userId}")
    public  ResponseEntity<User> getUserById(@RequestParam long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id:" + userId));
        return ResponseEntity.ok(user);
    }

//
//    @GetMapping
//    public List<User> getAllUser(){
//        return userRepository.findAll();
//    }







}