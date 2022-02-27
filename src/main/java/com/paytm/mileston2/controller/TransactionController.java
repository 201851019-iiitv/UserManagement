package com.paytm.mileston2.controller;


import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.exception.ResourceNotFoundException;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.service.TransactionService;
import com.paytm.mileston2.service.UserService;
import com.paytm.mileston2.service.WalletService;
import com.paytm.mileston2.utilities.validation.Validation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;


@RestController
public class TransactionController{

  final static Logger logger = Logger.getLogger(TransactionController.class.getName());

      @Autowired
      private WalletService walletService;

      @Autowired
      private TransactionService transactionService;

      @Autowired
      private UserService userService;

     //Kafka package
     @Autowired
     private KafkaTemplate<String,String> kafkaTemplate;

     private static final String TOPIC="txns";






    @PostMapping("/transaction")
    public CustomReturnType TransferMoney(@RequestBody Transaction txn){

           CustomReturnType msg =transactionService.TransferMoney(txn);

             // try to publish msg on kafka
        try {
            kafkaTemplate.send(TOPIC, "Money transferred successfully from your wallet Id :" + txn.payerWalletId + " amount: " + txn.getAmount() + " to walletId: " + txn.payeeWalletId);
        }
        catch (Exception e){
                throw new RuntimeException("can't push msg in  kafka");
        }
            return msg;
        }




    //Get all transaction summary by userId
    //url :http://localhost:8080/transaction?userId=<userId>

    @GetMapping("/transaction/{userId}")
    public ResponseEntity<List<Transaction>> getAllTxnsByUserId(@PathVariable Long userId, @RequestParam int pageNo){
        //return result in pagination form.
            Page<Transaction> txns = transactionService.getAllTxnsByUserId(userId,pageNo);
            logger.debug("retrieve all transaction by user Id" + userId);
      return ResponseEntity.ok(txns.getContent());
    }



    // get status of transaction by txnId
    //● url:http://localhost:8080/transaction?txnId=<txnID>

    @GetMapping("/transaction")
    public String getStatusByTxnId(@RequestParam Long txnId){

        // Done: check transaction id exist ?

        try {
            Transaction  txn = transactionService.getTxnsByTxnId(txnId);
            logger.debug("retrieve transaction status  by txn Id" + txnId);
            return txn.getStatus();
        }
        catch(Exception e){
            throw  new ResourceNotFoundException("Transaction Id not exist !");
        }

    }




}
