package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.DTO.CustomReturnType;
import Milestone2.Wallet_Management_Project.service.TransactionService;
import Milestone2.Wallet_Management_Project.service.UserService;
import Milestone2.Wallet_Management_Project.service.WalletService;
import Milestone2.Wallet_Management_Project.utilities.validation.Validation;
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
public class WalletController extends Validation {

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


    public  String getUsernameByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return currentUserName;
    }




    @PostMapping("/wallet/{mobileNumber}")
    public CustomReturnType createWallet(@PathVariable String mobileNumber){
        if(!mobileNumberValidation(mobileNumber))
            throw new BadRequestException("Invalid mobile number !");
        try{
            User user=userService.findByMobileno(mobileNumber); // check user exist with this mobile number ?


            // First get username from token and validity of token
            String requestTokenUserName=getUsernameByToken();

            // check userToken and with his details.
               if(user.getUsername().compareTo(requestTokenUserName)!=0) {
                   logger.error("user has unauthorized token !" +"with token : "+requestTokenUserName);
                   return new CustomReturnType("User Unauthorized ,Pls use your Token !", HttpStatus.BAD_REQUEST);
               }
                Optional<Wallet> TempW=walletService.getWalletById(mobileNumber);

            if(!TempW.isPresent()) {   // check wallet already exist ?
                Wallet w = new Wallet();
                w.setWalletId(mobileNumber);
                w.setCurr_bal(0.0F);
                walletService.createWallet(w);
                user.setWallet(w);
                userService.updateUser(user);
                logger.debug("user created wallet");
                logger.info("wallet Id: "+ mobileNumber);
                return new CustomReturnType("Wallet created successfully !", HttpStatus.CREATED);
            }
            else
            {
                logger.warn("user already wallet :" + mobileNumber);
                throw new BadRequestException("User has already Wallet !");
            }
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User does not exist with this mobile number ,Please check !");
        }


    }


    // Get all txns by wallet Id.
    @GetMapping("/wallet/{WalletId}/txns")
    public ResponseEntity<Page<Transaction>> getAllTransactionByWalletId(@PathVariable String WalletId,@RequestParam int pageNo){
        if(!mobileNumberValidation(WalletId))
            throw new BadRequestException("Invalid Wallet ID !");

        try {
            User user = userService.findByMobileno(WalletId);
            // First get username from token and validity of token
            String requestTokenUserName=getUsernameByToken();

            // check userToken and with his details.
            if(user.getUsername().compareTo(requestTokenUserName)!=0)
                throw new BadRequestException("User Unauthorized ,Pls use your Token !");
        }
        catch (Exception e){
            throw  new BadRequestException("Invalid WalletId !");
        }


             if(walletService.getWalletById(WalletId)!=null)
               try{
                   Page<Transaction> txns=transactionService.getAllTxnsByWalletId(WalletId,pageNo);
                   logger.debug("retrieve all transaction by wallet Id" +WalletId);
                   return ResponseEntity.ok(txns);
               }
               catch (Exception e){
                   throw  new BadRequestException("No transaction found with this wallet Id");
               }

             else{

                 throw  new ResourceNotFoundException("wallet Id does not exist !");
             }

    }

    // Add money in the user wallet
    @PostMapping("/wallet/{WalletId}/{amount}")
    public CustomReturnType AddMoney(@PathVariable String WalletId, @PathVariable  Float amount){

        if(!mobileNumberValidation(WalletId))
            throw new BadRequestException("Invalid Wallet ID !");

        try {
            User user = userService.findByMobileno(WalletId);
            // First get username from token and validity of token
            String requestTokenUserName=getUsernameByToken();

            // check userToken and with his details.
            if(user.getUsername().compareTo(requestTokenUserName)!=0)
                return new CustomReturnType("User Unauthorized ,Pls use your Token !",HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            throw  new BadRequestException("Invalid WalletId !");
        }

        //check Enter amount is positive ?

        if(amount<=0)
            throw  new BadRequestException("Amount is not valid !");

        //Check User wallet exist ?
        // if yes the add money otherwise can't be
        // Done:
        Wallet wallet =walletService.getWalletById(WalletId).orElseThrow(()-> new ResourceNotFoundException("wallet does not exist !"));

        try {
            // set balance in wallet.
            wallet.setCurr_bal(wallet.getCurr_bal() + amount);
            walletService.updateWallet(wallet);

            // create new txn for same walletId;
            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setTimestamp(new Timestamp(System.currentTimeMillis()));
            txn.setStatus("Success");
            txn.setPayeeWalletId("To Self   ");
            txn.setPayerWalletId(wallet.getWalletId());
            transactionService.createTxn(txn);
            logger.debug("Add money successfully in self wallet ");
            logger.info("amount : "+amount +" txnId :" +txn.getTxnId()+ " walletId: "+ wallet.getWalletId());
            // try to publish msg on kafka
            kafkaTemplate.send(TOPIC,"Money added successfully to your wallet Id :"+WalletId+" amount: "+txn.getAmount());

            return new CustomReturnType("Added money Successfully", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            throw new BadRequestException("can't be added money !");
        }
    }

    @GetMapping("/wallet/{mobileNumber}")
    public Optional<Wallet> getWalletDetailsById(@PathVariable String mobileNumber){

        logger.debug("retrieve wallet details by wallet Id" + mobileNumber);
        return walletService.getWalletById(mobileNumber);
    }


}
