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
import java.util.Optional;


@RestController
public class WalletController{

    final static Logger logger =Logger.getLogger(WalletController.class.getName());
    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    //Kafka package
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    private static final String TOPIC="wallet";


    @PostMapping("/wallet/{mobileNumber}")
    public CustomReturnType createWallet(@PathVariable String mobileNumber){
        try {
            return walletService.createWallet(mobileNumber);
        }
        catch (Exception e){
            logger.warn("wallet can't create ");
            throw  new BadRequestException("can't create wallet.");
        }
    }


    // Get all txns by wallet Id.
    @GetMapping("/wallet/{WalletId}/txns")
    public ResponseEntity<Page<Transaction>> getAllTransactionByWalletId(@PathVariable String WalletId,@RequestParam int pageNo){
        try {
            Page<Transaction> txns=transactionService.getAllTxnsByWalletId(WalletId,pageNo);
            logger.debug("retrieve all transaction by wallet Id" +WalletId);
            return ResponseEntity.ok(txns);
         }
        catch (Exception e){
            throw  new BadRequestException("Invalid WalletId !");
        }
    }

    // Add money in the user wallet
    @PostMapping("/wallet/{WalletId}/{amount}")
    public CustomReturnType AddMoney(@PathVariable String WalletId, @PathVariable  Float amount){

        try{
            CustomReturnType msg= walletService.AddMoney(WalletId,amount);
            try {
                // try to publish msg on kafka
                kafkaTemplate.send(TOPIC, "Money added successfully to your wallet Id :" + WalletId + " amount: " + amount);
            }
            catch (Exception e) {
                 throw new RuntimeException("can't push msg in  kafka");
            }
           return msg;
        }
        catch (Exception e){
            throw new BadRequestException("can't add money ");
        }
    }

    @GetMapping("/wallet/{mobileNumber}")
    public Wallet getWalletDetailsById(@PathVariable String mobileNumber){

        logger.debug("retrieve wallet details by wallet Id" + mobileNumber);
        return walletService.getWalletById(mobileNumber);
    }


}
