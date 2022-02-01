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
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Optional;


@RestController
public class WalletController {

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


    @RequestMapping(path= "/wallet/{mobileNumber}",method = RequestMethod.POST)
    public returnMssg createWallet(@PathVariable String mobileNumber){

        try{
            User user=userService.findByMobileno(mobileNumber); // check user exist with this mobile number ?

                Optional<Wallet> TempW=walletService.getWalletById(mobileNumber);

            if(!TempW.isPresent()) {   // check wallet already exist ?
                Wallet w = new Wallet();
                w.setWalletId(mobileNumber);
                w.setCurr_bal(0.0F);
                walletService.createWallet(w);
                user.setWallet(w);
                userService.updateUser(user);
                return new returnMssg("Wallet created successfully !", HttpStatus.CREATED);
            }
            else
            {
                throw new BadRequestException("User has already Wallet !");
            }
        }
        catch (Exception e){
            throw new ResourceNotFoundException("User does not exist with this mobile number ,Please check !");
        }


    }


    // Get all txns by wallet Id.
    @RequestMapping(path = "/wallet/{WalletId}/txns",method = RequestMethod.GET)
    public ResponseEntity<Page<Transaction>> getAllTransactionByWalletId(@PathVariable String WalletId,@RequestParam int pageNo){

             if(walletService.getWalletById(WalletId)!=null)
               try{
                   Page<Transaction> txns=transactionService.getAllTxnsByWalletId(WalletId,pageNo);

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
    @RequestMapping(path = "/wallet/{WalletId}/{amount}" ,method = RequestMethod.POST)
    public returnMssg AddMoney(@PathVariable String WalletId,@PathVariable  Float amount){


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

            // try to publish msg on kafka
            kafkaTemplate.send(TOPIC,"Money added successfully to your wallet Id :"+WalletId+" amount: "+txn.getAmount());

            return new returnMssg("Added money Successfully", HttpStatus.ACCEPTED);
        }
        catch (Exception e){
            throw new BadRequestException("can't be added money !");
        }
    }

}
