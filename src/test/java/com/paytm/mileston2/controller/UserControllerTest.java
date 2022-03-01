package com.paytm.mileston2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.DTO.JwtRequest;
import com.paytm.mileston2.DTO.JwtResponse;
import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {



    @Autowired
    MockMvc mockMvc;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    // for generate token .
    public  String  GenerateMockMvcToken(User user) throws Exception {
        // user -->username and password .
        String username=user.getUsername();
        String password=user.getPassword();

        JwtRequest jwtRequest=new JwtRequest(username,password);

        String requestTokenJson =objectMapper.writeValueAsString(jwtRequest);

        //GenrateToken for user.

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/GenerateTokens")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestTokenJson))
                .andExpect( MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent=result.getResponse().getContentAsString();
        JwtResponse token=objectMapper.readValue(resultContent,JwtResponse.class);

        String userToken =token.getToken();

        return userToken;
    }



    //Done work Perfectly
    @Test
    void createSuccessfulUser() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserCreateReq.json")));

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/user")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andReturn();
       String resultContent=result.getResponse().getContentAsString();
       CustomReturnType response=objectMapper.readValue(resultContent, CustomReturnType.class);
        Assert.assertEquals(response.getMsg(),"User created successfully.") ;
        //Delete user as well because it is only for testing purpose.
        //get json data for that string .
        JSONObject jsonObject = new JSONObject(requestJson);
        String mobileNumber = jsonObject.getString("mobileNumber");
        userService.deleteUserByMobileNumber(mobileNumber);

    }

    //Work perfectly
    @Test
    void getUserById() throws Exception {

        String userId="1";
        String userRes=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId" ,userId))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(userRes))
                        .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

       Long userId=7L;
       User user =userService.getUserById(userId);
        String userToken = GenerateMockMvcToken(user);


        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/user")
                        .param("userId" ,String.valueOf(userId))
                        .header(AUTHORIZATION,"Bearer "+userToken))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        Assert.assertEquals(msg.getMsg(),"User delete successfully");

        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);

    }


    @Test
    void updateUser() throws Exception {


        String userJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/UserDetails.json")));
        User user=objectMapper.readValue(userJson,User.class);
        String userToken = GenerateMockMvcToken(user);


        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .header(AUTHORIZATION,"Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        Assert.assertEquals(msg.getMsg(),"User updated successfully.");

        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);



    }

}