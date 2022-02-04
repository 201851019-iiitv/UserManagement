package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.DTO.CustomReturnType;
import Milestone2.Wallet_Management_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {



    @Autowired
    MockMvc mockMvc;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;
    @Test
    void createSuccessfulUser() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserCreateReq.json")));
        String ExpectedOutputJson = new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserCreateRes.json")));

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/user")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andReturn();
       String resultContent=result.getResponse().getContentAsString();
       CustomReturnType response=objectMapper.readValue(resultContent, CustomReturnType.class);
       CustomReturnType expectedResponse = objectMapper.readValue(ExpectedOutputJson ,CustomReturnType.class);
        Assert.assertEquals(expectedResponse.getMsg(),response.getMsg()) ;
        Assert.assertEquals(expectedResponse.getStatus() ,response.getStatus());
    }

    @Test
    void getUserById() throws Exception {

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId" ,"2"))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();
        String resultContent=result.getResponse().getContentAsString();

        CustomReturnType response=objectMapper.readValue(resultContent, CustomReturnType.class);



    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }



}