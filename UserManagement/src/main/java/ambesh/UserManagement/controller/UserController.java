package ambesh.UserManagement.controller;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import ambesh.UserManagement.model.User;
import ambesh.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }


    @GetMapping
    public  ResponseEntity<User> getUserById(@RequestParam long userId){
        System.out.println(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + userId));
        return ResponseEntity.ok(user);
    }

 @PutMapping
 public ResponseEntity<User> updateEmployee(@RequestParam long userId,@RequestBody User userDetails) {
     User updateUser = userRepository.findById(userId)
             .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

     updateUser.setUserName(userDetails.getUserName());
     updateUser.setFirstName(userDetails.getFirstName());
     updateUser.setLastName(userDetails.getLastName());
     updateUser.setMobileNumber(userDetails.getMobileNumber());
     updateUser.setEmailID(userDetails.getEmailID());
     updateUser.setAddress1(userDetails.getAddress1());
     updateUser.setAddress2(userDetails.getAddress2());


     userRepository.save(updateUser);

     return ResponseEntity.ok(updateUser);
 }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteEmployee(@RequestParam long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not exist with id: " + userId));

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



}