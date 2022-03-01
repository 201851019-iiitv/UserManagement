package com.paytm.mileston2.controller;

import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.model.Wallet;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.service.TransactionService;
import com.paytm.mileston2.service.UserService;
import com.paytm.mileston2.service.WalletService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


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
    @Operation(summary = "This method use to create new wallet for existing user .",description="to create new wallet .Required JWT Token ",security = @SecurityRequirement(name = "bearerAuth"))
    public CustomReturnType createWallet(@PathVariable String mobileNumber){
        try {
             logger.debug("request for mobile number ."+mobileNumber);
            return walletService.createWallet(mobileNumber);
        }
        catch (Exception e){
            logger.warn("wallet can't created ");
            throw  new BadRequestException(e.getMessage());
        }
    }


    // Get all txns by wallet Id.
    @GetMapping("/wallet/{WalletId}/txns")
    @Operation(summary = "This method use to get all transactions by user wallet Id .",description="to get all transactions .JWT Token is not required")
    public ResponseEntity<List<Transaction>> getAllTransactionByWalletId(@PathVariable String WalletId, @RequestParam int pageNo){
        try {
            Page<Transaction> txns=transactionService.getAllTxnsByWalletId(WalletId,pageNo);
            logger.debug("retrieve all transaction by wallet Id " +WalletId);
            return ResponseEntity.ok(txns.getContent());
         }
        catch (Exception e){
            throw  new BadRequestException("Invalid WalletId !");
        }
    }

    // Add money in the user wallet
    @PostMapping("/wallet/{WalletId}/{amount}")
    @Operation(summary = "This method use to add money to your wallet .",description="to add money in your wallet .Required JWT Token",security = @SecurityRequirement(name = "bearerAuth"))
    public CustomReturnType AddMoney(@PathVariable String WalletId, @PathVariable  Float amount){
        try{
            CustomReturnType msg= walletService.AddMoney(WalletId,amount);
            try {
                // try to publish msg on kafka
                if(msg.getStatus().compareTo(HttpStatus.ACCEPTED)==0)
                kafkaTemplate.send(TOPIC, "Money added successfully to your wallet Id :" + WalletId + " amount: " + amount+" & Timestamp : "+ Timestamp.from(Instant.now()));
            }
            catch (Exception e) {
                 throw new RuntimeException("can't push msg in  kafka");
            }
           return msg;
        }
        catch (Exception e){
            throw new BadRequestException("can't add money due to :  "+ e.getLocalizedMessage());
        }
    }

    @GetMapping("/wallet/{mobileNumber}")
    @Operation(summary = "This method use to get wallet Details by user walletId .",description="to get wallet details .Required JWT Token",security = @SecurityRequirement(name = "bearerAuth"))
    public Wallet getWalletDetailsById(@PathVariable String mobileNumber){

        logger.debug("retrieve wallet details by wallet Id" + mobileNumber);
        return walletService.getWalletById(mobileNumber);
    }


}
