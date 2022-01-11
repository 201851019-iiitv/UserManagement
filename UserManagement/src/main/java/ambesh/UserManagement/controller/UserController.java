package ambesh.UserManagement.controller;

import ambesh.UserManagement.model.User;
import ambesh.UserManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class UserController {
    @Autowired
    UserService userService;


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public HttpStatus insertUser(@RequestBody User user) {
        return userService.addUser(user) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
    }

    @RequestMapping(value = "/user?userId=/{id}", method = RequestMethod.GET)
    public @ResponseBody
    User getAllUsers(@PathVariable Long id) {
        return userService.getById(id);
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.getAllUsers();
    }


}