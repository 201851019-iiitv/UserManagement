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
    public HttpStatus createUser(@RequestBody User user) {

        if(user.validity().equals("completed")){
            if(!doesExist(user)) {
                userRepository.save(user);
                return HttpStatus.CREATED;

            }
            else
            {
                return HttpStatus.CONFLICT;
            }
        }

        return HttpStatus.BAD_REQUEST;
    }


    @GetMapping
    public  ResponseEntity<User> getUserById(@RequestParam long userId){
        System.out.println(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + userId));
        return ResponseEntity.ok(user);
    }

 @PutMapping
 public ResponseEntity<User> updateUser(@RequestParam long userId,@RequestBody User userDetails) {
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
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


// check user mobile number,username,email exist or not .
    public boolean doesExist(User user){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if((userlist.get(i).getUserName().equals(user.getUserName())) ||(userlist.get(i).getEmailID().equals(user.getEmailID()))||(userlist.get(i).getMobileNumber().equals(user.getMobileNumber())))
             return true;
        }
    return false;
    }


}