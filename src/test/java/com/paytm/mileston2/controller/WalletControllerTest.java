package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.service.WalletService;
import com.paytm.mileston2.utilities.FileUtilities;
import com.paytm.mileston2.utilities.ResultMatcher;
import com.paytm.mileston2.utilities.Token;
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
    private WalletService walletService;
    @Autowired
    private Token token;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createWallet() throws Exception {
        String mobileNumber = FileUtilities.getAttributeFromFile("mobileNumber.json", "mobileNumber");
        //generate token
        String userToken = token.GenerateMockMvcToken(mobileNumber);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/{mobileNumber}", mobileNumber)
                        .header(AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.isMatched(result.getResponse().getContentAsString(),"createWalletRes.json");
        Assert.assertTrue("User can't create wallet",output);

        //Delete wallet as well because it is only for testing purpose.
        walletService.DeleteWalletById(mobileNumber);

    }

    // Done Work Perfectly .
    @Test
    void addMoney() throws Exception {
        String mobileNumber = FileUtilities.getAttributeFromFile("addMoneyReq.json", "mobileNumber");
        String amount = FileUtilities.getAttributeFromFile("addMoneyReq.json", "amount");
        //generate token
        String userToken = token.GenerateMockMvcToken(mobileNumber);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/wallet/" + mobileNumber + "/" + amount)
                        .header(AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.isMatched(result.getResponse().getContentAsString(),"addMoneyRes.json");
        Assert.assertTrue("User can't add money in  wallet",output);
        //Substract add money from the wallet.
        // first find the wallet .
        Wallet w = walletService.getWalletById(mobileNumber);
        w.setCurrBal(w.getCurrBal() - Long.parseLong(amount));
        walletService.updateWallet(w);

    }


    @Test
    void getAllTransactionByWalletId() {


    }

    @Test
    void getWalletDetailsById() throws Exception {
        String walletResJson = FileUtilities.getJsonStringFromFile("walletDetails.json");
        Wallet wallet = (Wallet) FileUtilities.getObjectFromFile("walletDetails.json", Wallet.class);
        String mobileNumber = wallet.getWalletId();
        String userToken = token.GenerateMockMvcToken(mobileNumber);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wallet/{mobileNumber}", mobileNumber)
                        .header(AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(walletResJson))
                .andReturn();

    }
}