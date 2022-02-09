package ambesh.UserManagement.controller;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import ambesh.UserManagement.model.User;
import ambesh.UserManagement.service.UserService;
import ambesh.UserManagement.utilities.CustomReturnType;
import ambesh.UserManagement.utilities.validity;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class UserController extends validity {

    // create Logger
    private static Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public CustomReturnType createUser(@RequestBody User user) {
        //validate user mobile number
        if(!mobileNumberValidation(user.getMobileno()))
            throw new ResourceNotFoundException("Invalid mobile number !");
        if(!emailValidation(user.getEmail()))
            throw new ResourceNotFoundException("Invalid email number !");
        if(userService.findByMobileno(user.getMobileno())==null && userService.findByEmail(user.getEmail())==null ) {
            try {
                logger.debug("user created successfully "+user.getMobileno()+" "+user.getName());
                user.setStatus("Active");
                user.setCreateDate(new Date());
                userService.createUser(user);
                CustomReturnType mssg=new CustomReturnType("User created successfully !",HttpStatus.CREATED);
                return mssg;
            }
            catch (Exception e){
                throw  new ResourceNotFoundException("User can't be created !");
            }

        }

        throw new ResourceNotFoundException("User already exist!");
    }




    @GetMapping("/user")
    public  User getUserById(@RequestParam long userId){
        // Done:
        User user =userService.getUserById(userId).orElseThrow(()-> new ResourceNotFoundException("User does not exist with this Id !"));
        logger.debug("user retrieved by userId "+user.getUserId()+" "+user.getName());
        return user;
    }

    @PutMapping("/user")
    public  CustomReturnType updateUser(User user){
        if(!mobileNumberValidation(user.getMobileno()))
            throw new ResourceNotFoundException("Invalid mobile number !");
        if(!emailValidation(user.getEmail()))
            throw new ResourceNotFoundException("Invalid email number !");
        try {
            userService.updateUser(user);
            logger.debug("user updated successfully "+"User ID:  "+user.getUserId());
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User can't be Updated");
        }

        CustomReturnType mssg=new CustomReturnType("User delete successfully",HttpStatus.ACCEPTED);
        return mssg;
    }

    @DeleteMapping("/user")
    public CustomReturnType deleteUser(Long userId){

        User user=userService.getUserById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found !"));
        try {
            userService.deleteUser(user);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User can't be Deleted");
        }


        CustomReturnType mssg=new CustomReturnType("User delete successfully",HttpStatus.ACCEPTED);
        logger.debug("user deleted");
        return mssg;

    }






}
