package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.service.UserService;
import com.paytm.mileston2.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.mileston2.utilities.TestUtility;
import com.paytm.mileston2.utilities.Token;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private Token token;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createWallet() throws Exception {
       String jsonMobileNumber=TestUtility.getJsonStringFromFile("mobileNumber.json");
       JSONObject jsonObject=new JSONObject(jsonMobileNumber);
        String mobileNumber=jsonObject.getString("mobileNumber");
        //generate token
        String userToken = token.GenerateMockMvcToken(mobileNumber);

     MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/wallet/{mobileNumber}",mobileNumber)
             .header(AUTHORIZATION,"Bearer "+userToken))
             .andExpect(MockMvcResultMatchers.status().isOk())
             .andReturn();

     String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);


        Assert.assertEquals(msg.getMsg(),"wallet created successfully");


        //Delete wallet as well because it is only for testing purpose.
        walletService.DeleteWalletById(mobileNumber);


    }

    // Done Work Perfectly .
    @Test
    void addMoney() throws Exception {

        String jsonMobileNumber=TestUtility.getJsonStringFromFile("mobileNumber.json");
        JSONObject jsonObject=new JSONObject(jsonMobileNumber);
        String mobileNumber=jsonObject.getString("mobileNumber");
        Long amount=3L;
        //generate token
        String userToken = token.GenerateMockMvcToken(mobileNumber);

        final String requestUrl ="/wallet/"+mobileNumber+"/"+String.valueOf(amount);
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post(requestUrl)
                        .header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        Assert.assertEquals(msg.getMsg(),"money added successfully in your wallet ");

      //Substract add money from the wallet.
       // first find the wallet .
       Wallet w =walletService.getWalletById(mobileNumber);
       w.setCurrBal(w.getCurrBal()-amount);
       walletService.updateWallet(w);

    }


    @Test
    void getAllTransactionByWalletId() {


    }

    @Test
    void getWalletDetailsById() throws Exception {
        String walletResJson= TestUtility.getJsonStringFromFile( "WalletDetails.json");
        Wallet wallet= (Wallet) TestUtility.getObjectFromFile("WalletDetails.json",Wallet.class);
        String mobileNumber=wallet.getWalletId();
        String userToken = token.GenerateMockMvcToken(mobileNumber);

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/wallet/{mobileNumber}",mobileNumber)
                        .header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(walletResJson))
                .andReturn();


    }
}