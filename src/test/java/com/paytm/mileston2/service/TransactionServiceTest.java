package com.paytm.mileston2.service;

import com.paytm.mileston2.DAO.TransactionDao;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.Repository.TransactionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)

class TransactionServiceTest {

    @MockBean
    private TransactionDao transactionDao;

    @InjectMocks
    private TransactionService transactionService;


     ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getTxnByTxnIDTest() throws IOException {
        String s=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/TransactionDetails.json")));
        Transaction t=objectMapper.readValue(s,Transaction.class);
          Mockito.when(transactionDao.findTxnByTxnId(t.getTxnId())).thenReturn(t);
     Transaction t1= transactionService.getTxnsByTxnId(t.getTxnId());

        Assert.assertEquals(t,t1);

    }


    @Test
    public void createTxnTest() throws IOException {
        String s=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/TransactionDetails.json")));
        Transaction t=objectMapper.readValue(s,Transaction.class);
        Mockito.when(transactionDao.saveTxn(t)).thenReturn(t);
        Assert.assertEquals(transactionService.createTxn(t),t);
    }


    //ToDo: pagable tests.
    @Test
    void getAllTxnsByWalletIdTest() throws IOException {

        String walletId="1234567890";
        int pageNo=0;
        String s=new String(Files.readAllBytes(Paths.get("src/test/resources/SampleData/TransactionDetails.json")));
        Transaction t=objectMapper.readValue(s,Transaction.class);
        Pageable pageable = PageRequest.of(pageNo,2);
       // Mockito.when(transactionDao.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable)).thenReturn(t);
        Assert.assertEquals(transactionService.getAllTxnsByWalletId(walletId,pageNo),t);

    }

    //ToDo: pagable tests.
    @Test
    void getAllTxnsByUserIdTest() {
    }


    @Test
    void transferMoneyTest() {

    }
}