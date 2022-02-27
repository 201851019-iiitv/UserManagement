package com.paytm.mileston2.service;

import com.paytm.mileston2.DTO.ReqUser;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.Repository.UserRepo;
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


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {


    @MockBean
    private UserRepo userRepo;

    @InjectMocks
    private UserService userService;

    ObjectMapper objectMapper = new ObjectMapper();



    @Test
    void createUser() throws IOException {

        String userJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userRepo.save(user)).thenReturn(user);
        Assert.assertEquals(userService.createUser(user),user);

    }

    @Test
    void findByMobileno() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userRepo.findByMobileNumber(user.getMobileNumber())).thenReturn(user);
        Assert.assertEquals(userService.findByMobileNumber(user.getMobileNumber()),user);
    }

    @Test
    void findByEmail() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
        Assert.assertEquals(userService.findByEmail(user.getEmail()),user);

    }

    @Test
    void findUserByUsername() throws IOException {
        String userJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDetails.json")));

        User user=objectMapper.readValue(userJson,User.class);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);
        Assert.assertEquals(userService.findUserByUsername(user.getUsername()),user);

    }
}