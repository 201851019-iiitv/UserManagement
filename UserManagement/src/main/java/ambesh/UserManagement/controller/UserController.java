package ambesh.UserManagement.controller;

import ambesh.UserManagement.Validation.validity;
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
public class UserController extends validity {

    @Autowired
    private UserRepository userRepository;


    @PostMapping
    public HttpStatus createUser(@RequestBody User user) {

        if(validation(user)){
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
    public  String getUserById(@RequestParam long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + userId));
        return user.getFirstName() +","+user.getLastName()+","+user.getMobileNumber()+","+user.getEmailID();
    }

 @PutMapping
 public HttpStatus updateUser(@RequestParam long userId,@RequestBody User userDetails) {
     User updateUser = userRepository.findById(userId)
             .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

     boolean dataCorrect=true;
     if(dataCorrect && userDetails.getUserName()!=null && doesUserExist(userDetails.getUserName()))
         dataCorrect=false;

     if(dataCorrect && userDetails.getEmailID()!=null && doesEmailIDExist(userDetails.getEmailID()))
         dataCorrect=false;
     if(dataCorrect && userDetails.getMobileNumber()!=null && doesMobileExist(userDetails.getMobileNumber()))
         dataCorrect=false;

     if(dataCorrect) {

         if(userDetails.getUserName()!=null){
             updateUser.setUserName(userDetails.getUserName());
         }
         if(userDetails.getFirstName()!=null){
             updateUser.setFirstName(userDetails.getFirstName());
         }
         if(userDetails.getLastName()!=null){
             updateUser.setLastName(userDetails.getLastName());
         }
         if(userDetails.getEmailID()!=null)
             updateUser.setEmailID(userDetails.getEmailID());
         if(userDetails.getMobileNumber()!=null)
             updateUser.setMobileNumber(userDetails.getMobileNumber());
         if(userDetails.getAddress1()!=null){
             updateUser.setAddress1(userDetails.getAddress1());
         }
         if(userDetails.getAddress2()!=null){
             updateUser.setAddress2(userDetails.getAddress2());
         }


         updateUser.setMobileNumber(userDetails.getMobileNumber());
         updateUser.setEmailID(userDetails.getEmailID());
         userRepository.save(updateUser);

         return HttpStatus.OK;

     }


     return HttpStatus.BAD_REQUEST;

 }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteUser(@RequestParam long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + userId));

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }


}