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

@RestController
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(path= "/wallet/{mobileNumber}",method = RequestMethod.POST)
    public ResponseEntity<?> createWallet(@PathVariable String mobileNumber){

        // this will not while it is string class.
        //System.out.println(mobileNumber.getClass().getSimpleName());
       User user=userService.findByMobileno(mobileNumber);
        if(user!=null) {
            Wallet w=new Wallet();
            w.setWalletId(mobileNumber);
            w.setCurr_bal(0.0F);
            walletService.createWallet(w);
            user.setWallet(w);
            userService.updateUser(user);



        }

        return new ResponseEntity<>("Wallet created successfully !",HttpStatus.ACCEPTED);

    }


    // Get all txns by wallet Id.
    @RequestMapping(path = "/wallet/{WalletId}/txns",method = RequestMethod.GET)
    public List<Transaction> getAllTransactionByWalletId(@PathVariable String WalletId){

        return transactionService.getAllTxnsByWalletId(WalletId);

    }

    // Add money in the user wallet
    @RequestMapping(path = "/wallet/{WalletId}/{amount}" ,method = RequestMethod.POST)
    public ResponseEntity<?> AddMoney(@PathVariable String WalletId,@PathVariable  Float amount){

        //Check User wallet exist ?
        // if yes the add money otherwise can't be
        //check Enter amount is positive ?

        if(amount<=0)
            return new ResponseEntity<>("Amount is not valid !", HttpStatus.BAD_REQUEST);

        // Todo:
        Wallet wallet =walletService.getWalletById(WalletId).orElseThrow();

        // set balance in wallet.
        wallet.setCurr_bal(wallet.getCurr_bal()+amount);
        walletService.updateWallet(wallet);

        // create new txn for same walletId;
         Transaction txn=new Transaction();
         txn.setAmount(amount);
         txn.setTimestamp(new Timestamp(System.currentTimeMillis()));
         txn.setStatus("Success");
         txn.setPayeeWalletId("To Self   ");
         txn.setPayerWalletId(wallet.getWalletId());
         transactionService.createTxn(txn);

        return new ResponseEntity<>("Added money Successfully",HttpStatus.ACCEPTED);

    }

}