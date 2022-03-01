package com.paytm.mileston2.service;

import com.paytm.mileston2.DAO.WalletDao;
import com.paytm.mileston2.model.Wallet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class WalletServiceTest {

    @MockBean
    private WalletDao walletDao;

    @InjectMocks
    private WalletService walletService;


     ObjectMapper objectMapper=new ObjectMapper();


    @Test
    void createWallet() throws IOException {

        String walletJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/WalletDetails.json")));

        Wallet wallet =objectMapper.readValue(walletJson,Wallet.class);
        Mockito.when(walletDao.saveWallet(wallet)).thenReturn(wallet);
        Assert.assertEquals(walletService.createWallet(wallet.getWalletId()).getMsg(),"wallet created successfully");
    }

    @Test
    void getWalletById() throws IOException {

        String walletJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/WalletDetails.json")));

        Wallet wallet =objectMapper.readValue(walletJson,Wallet.class);
        Mockito.when(walletDao.findWalletById(wallet.getWalletId())).thenReturn(wallet);
        Wallet w=walletService.getWalletById(wallet.getWalletId());
        Assert.assertEquals(w,wallet);
    }

    @Test
    void updateWallet() throws IOException {

        String walletJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/WalletDetails.json")));

        Wallet wallet =objectMapper.readValue(walletJson,Wallet.class);
        Mockito.when(walletDao.saveWallet(wallet)).thenReturn(wallet);
        Assert.assertEquals(walletService.updateWallet(wallet),wallet);
    }

    @Test
    void deleteWalletById() throws IOException {
        String walletJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/WalletDetails.json")));

        Wallet wallet =objectMapper.readValue(walletJson,Wallet.class);
        Mockito.when(walletDao.deleteWallet(wallet)).thenReturn(wallet);
        Assert.assertEquals(walletService.updateWallet(wallet),wallet);
    }



    @Test
    void addMoney() throws IOException {
        String walletJson=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/WalletDetails.json")));

        Wallet wallet =objectMapper.readValue(walletJson,Wallet.class);
        Mockito.when(walletDao.saveWallet(wallet)).thenReturn(wallet);
        Assert.assertEquals(walletService.AddMoney(wallet.getWalletId(),wallet.getCurrBal()),wallet);
    }
}