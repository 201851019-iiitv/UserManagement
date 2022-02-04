package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.DTO.JwtRequest;
import Milestone2.Wallet_Management_Project.DTO.JwtResponse;
import Milestone2.Wallet_Management_Project.model.User;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


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
        String mobileNumber="9123456785";

        //generate token
        String userToken = GenerateMockMvcToken(mobileNumber);


     MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/wallet/{mobileNumber}",mobileNumber)
             .header(AUTHORIZATION,"Bearer "+userToken))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andReturn();

     String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        // Response class .
        String walletResJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/createWalletRes.json")));
        CustomReturnType resMsg =objectMapper.readValue(walletResJson, CustomReturnType.class);

        Assert.assertEquals( resMsg.getMsg(),msg.getMsg());
        Assert.assertEquals(resMsg.getStatus(),msg.getStatus());



    }

    // Done Work Perfectly .
    @Test
    void addMoney() throws Exception {

        String mobileNumber="9123456781";
        Long amount=10L;
        //generate token
        String userToken = GenerateMockMvcToken(mobileNumber);

        final String requestUrl ="/wallet/"+mobileNumber+"/"+String.valueOf(amount);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        // Response class .
        String walletResJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/AddMoneyRes.json")));
        CustomReturnType resMsg =objectMapper.readValue(walletResJson, CustomReturnType.class);


        Assert.assertEquals(resMsg.getMsg() ,msg.getMsg());
        Assert.assertEquals(resMsg.getStatus(),msg.getStatus());




    }


}