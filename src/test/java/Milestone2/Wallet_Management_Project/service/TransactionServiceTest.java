package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.repository.TransactionRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void getTxnByTxnIDTest(){
        Long txnId=5L;

        Transaction t=new Transaction();
          t.setStatus("Success");
          t.setAmount(10F);
          t.setTxnId(txnId);
          t.setPayeeWalletId("9123456780");
          t.setPayerWalletId("1234567890");
          t.setTimestamp(java.sql.Timestamp.valueOf("2022-01-27 10:09:58.512000"));

          Mockito.when(transactionRepo.findByTxnId(5L)).thenReturn(t);
     Transaction t1= transactionService.getTxnsByTxnId(txnId);

        Assert.assertEquals(t,t1);

    }


    @Test
    public void createTxnTest() throws IOException {

        Long txnId=25L;
        Transaction t=new Transaction();
        t.setStatus("Success");
        t.setAmount(10F);
        t.setTxnId(txnId);
        t.setPayeeWalletId("9123456780");
        t.setPayerWalletId("1234567890");
        t.setTimestamp(java.sql.Timestamp.valueOf("2022-01-27 10:09:58.512000"));

//        String s=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/createTransactionServiceTest.json")));
//        Transaction t=objectMapper.readValue(s,Transaction.class);
        Mockito.when(transactionRepo.save(t)).thenReturn(t);
        Assert.assertEquals(transactionService.createTxn(t),t);
        //Assert.assertNotNull(transactionRepo.findByTxnId(19L));
       //transactionRepo.delete(t);
    }



}