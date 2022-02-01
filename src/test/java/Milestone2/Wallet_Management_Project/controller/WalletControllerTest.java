package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.JWT.model.JwtRequest;
import Milestone2.Wallet_Management_Project.JWT.model.JwtResponse;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.returnPackage.returnMssg;
import Milestone2.Wallet_Management_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.protocol.types.Field;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;


@SpringBootTest
@AutoConfigureMockMvc

class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;


    // for generate token .
    public  String  GenerateMockMvcToken(String userWalletId) throws Exception {
        // From payerWalletId -->get user.
        User user=userService.findByMobileno(userWalletId);

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



    @Test
    void createWallet() throws Exception {
        String mobileNumber="9123456781";

        //generate token
        String userToken = GenerateMockMvcToken(mobileNumber);


     MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/wallet/{mobileNumber}",mobileNumber)
             .header(AUTHORIZATION,"Bearer "+userToken))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andReturn();

     String response =result.getResponse().getContentAsString();
        returnMssg msg =objectMapper.readValue(response,returnMssg.class);

        Assert.assertEquals("Wallet created successfully !" ,msg.getMsg());
        Assert.assertEquals(HttpStatus.CREATED ,msg.getStatus());



    }

    @Test
    void addMoney() throws Exception {

    }


}