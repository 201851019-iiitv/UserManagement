package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.returnPackage.returnMssg;
import Milestone2.Wallet_Management_Project.service.TransactionService;
import Milestone2.Wallet_Management_Project.service.UserService;
import Milestone2.Wallet_Management_Project.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.List;


@RestController
public class TransactionController {

      @Autowired
      private WalletService walletService;

      @Autowired
      private TransactionService transactionService;

     @Autowired
     private UserService userService;


    @RequestMapping(path = "/transaction",method = RequestMethod.POST)
    public ResponseEntity<returnMssg> TransferMoney(@RequestBody Transaction txn){

        // check requested amount is positive ?
        if(txn.getAmount()<=0) {
            returnMssg mssg=new returnMssg("Please request positive amount value",HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(mssg);
        }

         String payer_walletId= txn.getPayerWalletId();
         String payee_walletId= txn.getPayeeWalletId();

         // Done : Handle error .
         Wallet payerWallet =walletService.getWalletById(payer_walletId).orElseThrow(()-> new ResourceNotFoundException("No Wallet Found at payer mobile number please check!"));

        Wallet payeeWallet =walletService.getWalletById(payee_walletId).orElseThrow(()-> new ResourceNotFoundException("No Wallet Found at payee mobile number please check!"));;




        // check payer have sufficient amount request ?
        Float currBalofPayer=payerWallet.getCurr_bal();

        if(currBalofPayer< txn.getAmount()){
            returnMssg mssg=new returnMssg("Insufficient Balance !",HttpStatus.BAD_REQUEST);
            return ResponseEntity.ok(mssg);
        }

        Float currBalofPayee=payeeWallet.getCurr_bal();

        // it create temp replication data when server failed in b\w execution.
        Wallet TempPayerWallet=payerWallet;
        Wallet TempPayeeWallet=payeeWallet;

       try {
        currBalofPayee += txn.getAmount();
        currBalofPayer -= txn.getAmount();

            payerWallet.setCurr_bal(currBalofPayer);
            payeeWallet.setCurr_bal(currBalofPayee);


            //Done : use try catch for that
            //Update wallet
            walletService.updateWallet(payeeWallet);
            walletService.updateWallet(payerWallet);

            txn.setStatus("Success");
            txn.setTimestamp(new Timestamp(System.currentTimeMillis()));
            transactionService.createTxn(txn);
            returnMssg mssg = new returnMssg("Money transferred successfully", HttpStatus.ACCEPTED);
            return ResponseEntity.ok(mssg);
        }
       catch (Exception e){
           walletService.updateWallet(TempPayeeWallet);
           walletService.updateWallet(TempPayerWallet);

           txn.setStatus("Failed");
           txn.setTimestamp(new Timestamp(System.currentTimeMillis()));
           transactionService.createTxn(txn);

            throw  new BadRequestException("Unsuccessful transaction !");
       }

    }

    //Get all transaction summary by userId
    //url :http://localhost:8080/transaction?userId=<userId>

    @RequestMapping(path = "/transaction/{userId}",method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> getAllTxnsByUserId(@PathVariable Long userId, @RequestParam int pageNo){

        //Done:
        User user=userService.getUserById(userId).orElseThrow(()-> new ResourceNotFoundException("No user find please check user id !"));

        // check user have wallet  or not ?
        String userWalletId;
        try {
            userWalletId = user.getWallet().getWalletId();

        }
        catch (Exception e){
            throw new ResourceNotFoundException("user haven't wallet !");
        }
            // it can be empty.

        //Done:
        //return result in pagination form.
            Page<Transaction> txns = transactionService.getAllTxnsByWalletId(userWalletId,pageNo);



   return ResponseEntity.ok(txns.getContent());
    }



    // get status of transaction by txnId
    //● url:http://localhost:8080/transaction?txnId=<txnID>

    @RequestMapping(path = "/transaction",method = RequestMethod.GET)
    public ResponseEntity<?> getStatusByTxnId(@RequestParam Long txnId){

        // Done: check transaction id exist ?
        Transaction txn;
        try {
            txn = transactionService.getTxnsByTxnId(txnId);

        }
        catch(Exception e){
            throw  new ResourceNotFoundException("Transaction Id not exist !");
        }

            return ResponseEntity.ok(txn.getStatus());

    }




}
