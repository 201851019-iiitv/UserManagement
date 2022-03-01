package com.paytm.mileston2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.mileston2.DAO.UserDao;
import com.paytm.mileston2.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {


    @MockBean
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();



    @Test
    void createUser() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.saveUser(user)).thenReturn(user);
        Assert.assertEquals(userService.createUser(user).getMsg(),"User created successfully.");
    }

    @Test
    void findByMobileNumber() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.findByMobileNumber(user.getMobileNumber())).thenReturn(user);
        Assert.assertEquals(userService.findByMobileNumber(user.getMobileNumber()),user);
    }

    @Test
    void findByEmail() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.findByEmail(user.getEmail())).thenReturn(user);
        Assert.assertEquals(userService.findByEmail(user.getEmail()),user);

    }

    @Test
    void findUserByUsername() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.findByUsername(user.getUsername())).thenReturn(user);
        Assert.assertEquals(userService.findUserByUsername(user.getUsername()),user);

    }

    @Test
    void getAllUser() throws IOException {
       List<User> users = new ArrayList<>();
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        users.add(user);
        Mockito.when(userDao.findAllUsers()).thenReturn(users);
        Assert.assertEquals(userService.getAllUser(),users);
    }

    @Test
    void getUserById() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.findUserById(user.getUserId())).thenReturn(user);
        Assert.assertEquals(userService.getUserById(user.getUserId()),user);
    }

    @Test
    void updateUser() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.saveUser(user)).thenReturn(user);
        Assert.assertEquals(userService.updateUser(user).getMsg(),"User updated successfully.");
    }

    @Test
    void deleteUser() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.deleteUser(user.getUserId())).thenReturn(user);
        Assert.assertEquals(userService.deleteUser(user.getUserId()).getMsg(),"User delete successfully");
    }

    @Test
    void deleteUserByMobileNumber() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userDao.deleteUser(user.getUserId())).thenReturn(user);
        Assert.assertEquals(userService.deleteUserByMobileNumber(user.getMobileNumber()),user);
    }
}