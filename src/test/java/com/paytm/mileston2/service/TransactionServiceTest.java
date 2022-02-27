package com.paytm.mileston2.service;

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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)

class TransactionServiceTest {

    @MockBean
    private TransactionRepo transactionRepo;

    @InjectMocks
    private TransactionService transactionService;


     ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getTxnByTxnIDTest() throws IOException {
        String s=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/TransactionDetails.json")));
        Transaction t=objectMapper.readValue(s,Transaction.class);
          Mockito.when(transactionRepo.findByTxnId(t.getTxnId())).thenReturn(t);
     Transaction t1= transactionService.getTxnsByTxnId(t.getTxnId());

        Assert.assertEquals(t,t1);

    }


    @Test
    public void createTxnTest() throws IOException {
        String s=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/TransactionDetails.json")));
        Transaction t=objectMapper.readValue(s,Transaction.class);
        Mockito.when(transactionRepo.save(t)).thenReturn(t);
        Assert.assertEquals(transactionService.createTxn(t),t);
    }



}