package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.JWT.model.JwtRequest;
import Milestone2.Wallet_Management_Project.JWT.model.JwtResponse;
import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.returnPackage.returnMssg;
import Milestone2.Wallet_Management_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.net.http.HttpHeaders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest {

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
    void transferMoney() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/java/DTO/TransferMoneyReq.json")));
        // First generateToken of payer by its walletId.

        Transaction txns=objectMapper.readValue(requestJson,Transaction.class);
       //get PayerWalletId
       String userWalletId= txns.getPayerWalletId();


       //call GenerateMockMvcToken
        String userToken=GenerateMockMvcToken(userWalletId);


        // TransferMoney Api


      MvcResult result1=  mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson).header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String resultContent1=result1.getResponse().getContentAsString();
        returnMssg response=objectMapper.readValue(resultContent1,returnMssg.class);
        Assert.assertEquals("Money transferred successfully",response.getMsg()) ;
        Assert.assertEquals(HttpStatus.ACCEPTED ,response.getStatus());



    }

    @Test
    void getAllTxnsByUserId() {






    }

    @Test
    void getStatusByTxnId() {
    }


}