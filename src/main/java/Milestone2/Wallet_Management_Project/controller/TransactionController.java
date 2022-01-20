package Milestone2.Wallet_Management_Project.controller;
import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.service.TransactionService;
import Milestone2.Wallet_Management_Project.service.UserService;
import Milestone2.Wallet_Management_Project.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
public class TransactionController {

      @Autowired
      private WalletService walletService;

      @Autowired
      private TransactionService transactionService;

     @Autowired
     private UserService userService;


    @RequestMapping(path = "/transaction",method = RequestMethod.POST)
    public ResponseEntity<?> TransferMoney(@RequestBody Transaction txn){

         String payer_walletId= txn.getPayerWalletId();
         String payee_walletId= txn.getPayeeWalletId();

         // Todo : Handle error .
         Wallet payerWallet =walletService.getWalletById(payer_walletId).orElseThrow();

        Wallet payeeWallet =walletService.getWalletById(payee_walletId).orElseThrow();


        // check requested amount is positive ?
        if(txn.getAmount()<=0)
            return new ResponseEntity<>("Please request positive amount value", HttpStatus.BAD_REQUEST);

//         //check payer wallet exist ?
//        if(!payerWallet.isPresent())
//            return new ResponseEntity<>("Please request correct mobile number\n From this mobile number no wallet Found !", HttpStatus.BAD_REQUEST);
//
//        // check payee wallet exist ?
//
//        if(!payeeWallet.isPresent())
//            return new ResponseEntity<>("Please request correct mobile number\n From this mobile number no wallet Found !", HttpStatus.BAD_REQUEST);


        // check payer have sufficient amount request ?
        Float currBalofPayer=payerWallet.getCurr_bal();

        if(currBalofPayer< txn.getAmount())
            return new ResponseEntity<>("Insufficient Balance !", HttpStatus.BAD_REQUEST);

        Float currBalofPayee=payeeWallet.getCurr_bal();

        currBalofPayee += txn.getAmount();
        currBalofPayer -= txn.getAmount();

     payerWallet.setCurr_bal(currBalofPayer);
     payeeWallet.setCurr_bal(currBalofPayee);


     //Todo : use try catch for that
     //Update wallet
        walletService.updateWallet(payeeWallet);
        walletService.updateWallet(payerWallet);

        txn.setStatus("Success");
        txn.setTimestamp(new Timestamp(System.currentTimeMillis()));
        transactionService.createTxn(txn);
    return new ResponseEntity<>("Money transferred successfully" ,HttpStatus.ACCEPTED);

    }

    //Get all transaction summary by userId
    //url :http://localhost:8080/transaction?userId=<userId>

    @RequestMapping(path = "/transaction/{userId}",method = RequestMethod.GET)
    public ResponseEntity<List<Transaction>> getAllTxnsByUserId(@PathVariable Long userId){

        //Todo:
        User user=userService.getUserById(userId).orElseThrow();

        // check userwallet is exist or not ?
        String userWalletId= user.getWallet().getWalletId();

        // it can be empty.
        List<Transaction> txns=transactionService.getAllTxnsByWalletId(userWalletId);

        //ToDo:
        //return result in pagination form.


   return ResponseEntity.ok(txns);
    }



    // get status of transaction by txnId
    //● url:http://localhost:8080/transaction?txnId=<txnID>

    @RequestMapping(path = "/transaction",method = RequestMethod.GET)
    public ResponseEntity<?> getStatusByTxnId(@RequestParam Long txnId){

        // Todo: check transaction id exist ?
        Transaction txn=transactionService.getTxnsByTxnId(txnId);


        return ResponseEntity.ok(txn.getStatus());
    }




}
