package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.service.WalletService;
import com.paytm.mileston2.utilities.FileUtilities;
import com.paytm.mileston2.utilities.Token;
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
    private WalletService walletService;

    @Autowired
    private Token token;


    //Done Work perfectly
    @Test
    void transferMoney() throws Exception {
        String requestJson = FileUtilities.getJsonStringFromFile("transferMoneyReq.json");
        String payerWalletId = FileUtilities.getAttributeFromFile("transferMoneyReq.json", "payerWalletId");
        String payeeWalletId = FileUtilities.getAttributeFromFile("transferMoneyReq.json", "payeeWalletId");
        String amount = FileUtilities.getAttributeFromFile("transferMoneyReq.json", "amount");
        // GenerateMockMvcToken
        String userToken = token.GenerateMockMvcToken(payerWalletId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson).header(AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CustomReturnType actualResponse = (CustomReturnType) FileUtilities.getObjectFromjsonString(result.getResponse().getContentAsString(), CustomReturnType.class);
        CustomReturnType expectedResponse = (CustomReturnType) FileUtilities.getObjectFromFile("transferMoneyRes.json", CustomReturnType.class);

        Assert.assertEquals(expectedResponse.getMsg(), actualResponse.getMsg());
        Assert.assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());


        // return money back to  account only for testing only .
        String TempPayeeWalletId = payerWalletId;
        String TempPayerWalletId = payeeWalletId;
        Float TempAmount = Float.parseFloat(amount);
        //payerWallet
        Wallet TempPayerWallet = walletService.getWalletById(TempPayerWalletId);
        TempPayerWallet.setCurrBal(TempPayerWallet.getCurrBal() - TempAmount);
        walletService.updateWallet(TempPayerWallet);
        //change payeeWallet
        Wallet TempPayeeWallet = walletService.getWalletById(TempPayeeWalletId);
        TempPayeeWallet.setCurrBal(TempPayeeWallet.getCurrBal() + TempAmount);
        walletService.updateWallet(TempPayeeWallet);

    }


    //Done: how can pass long value in requesturl.
    @Test
    void getStatusByTxnId() throws Exception {

        String txnId = FileUtilities.getAttributeFromFile("transactionStatus.json", "txnId");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/transaction").param("txnId", txnId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();
        String expectedResponse = FileUtilities.getAttributeFromFile("transactionStatus.json", "status");
        Assert.assertEquals(expectedResponse, actualResponse);
    }


    @Test
    void getAllTxnsByUserId() {
    }
}