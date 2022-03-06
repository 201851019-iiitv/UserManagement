package com.paytm.mileston2.controller;

import com.paytm.mileston2.model.Transaction;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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

     @Autowired
     private Token token;


    //Done Work perfectly
    @Test
    void transferMoney() throws Exception {
        String requestJson =TestUtility.getJsonStringFromFile("TransferMoneyReq.json");
        Transaction txns= (Transaction) TestUtility.getObjectFromFile("TransferMoneyReq.json",Transaction.class);
       //get PayerWalletId
       String userWalletId= txns.getPayerWalletId();
       // GenerateMockMvcToken
        String userToken=token.GenerateMockMvcToken(userWalletId);
        // TransferMoney Api
      MvcResult result=  mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestJson).header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        String resultContent=result.getResponse().getContentAsString();
        CustomReturnType response = (CustomReturnType) TestUtility.getObjectFromjsonString(resultContent,CustomReturnType.class);
        Assert.assertEquals(response.getMsg(),"money transfer successfully") ;

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


    @Test
    void getAllTxnsByUserId() {
    }
}