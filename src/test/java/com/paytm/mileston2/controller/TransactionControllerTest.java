package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.JwtRequest;
import com.paytm.mileston2.DTO.JwtResponse;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.service.UserService;
import com.paytm.mileston2.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
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
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

   // for generate token .
    public  String  GenerateMockMvcToken(String userWalletId) throws Exception {
        // From payerWalletId -->get user.
        User user=userService.findByMobileNumber(userWalletId);

        // user -->username and password .
        String username=user.getUsername();
        String password=user.getPassword();

        JwtRequest jwtRequest=new JwtRequest(username,password);

        String requestTokenJson =objectMapper.writeValueAsString(jwtRequest);

        //GenerateToken for user.

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



    //Done Work perfectly
    @Test
    void transferMoney() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/java/DTO/TransferMoneyReq.json")));
        String responseJson =new String(Files.readAllBytes(Paths.get("src/test/java/DTO/TransferMoneyRes.json")));
        // First generateToken of payer by its walletId.

        Transaction txns=objectMapper.readValue(requestJson,Transaction.class);
       //get PayerWalletId
       String userWalletId= txns.getPayerWalletId();


       //call GenerateMockMvcToken
        String userToken=GenerateMockMvcToken(userWalletId);


        // TransferMoney Api


      MvcResult result=  mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson).header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        String resultContent=result.getResponse().getContentAsString();
        CustomReturnType response=objectMapper.readValue(resultContent, CustomReturnType.class);
        CustomReturnType response1=objectMapper.readValue(responseJson, CustomReturnType.class);
        Assert.assertEquals(response1.getMsg(),response.getMsg()) ;
        Assert.assertEquals(response1.getStatus() ,response.getStatus());

       // return money back to  account only for testing only .
        //get json data for that string .
        JSONObject jsonObject = new JSONObject(requestJson);
        String payeeWalletId= jsonObject.getString("payerWalletId");
        String payerWalletId= jsonObject.getString("payeeWalletId");
        Float amount=Float.parseFloat(jsonObject.getString("amount"));

        //change payerWallet
        Wallet payerWallet= walletService.getWalletById(payerWalletId);
        payerWallet.setCurrBal(payerWallet.getCurrBal()-amount);
        walletService.updateWallet(payerWallet);
        //change payeeWallet
        Wallet payeeWallet= walletService.getWalletById(payeeWalletId);
        payeeWallet.setCurrBal(payeeWallet.getCurrBal()+amount);
        walletService.updateWallet(payeeWallet);



    }


    //Done: how can pass long value in requesturl.
    @Test
    void getStatusByTxnId() throws Exception {

        Long txnId=16L;
        MvcResult result=  mockMvc.perform(MockMvcRequestBuilders.get("/transaction").param("txnId",String.valueOf(txnId)))
                                  .andExpect(MockMvcResultMatchers.status().isOk())
                                  .andReturn();

        String resultJson=result.getResponse().getContentAsString();
      Assert.assertEquals(resultJson,"Success");
    }


}