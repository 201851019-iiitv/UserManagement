package com.paytm.mileston2.service;

import com.paytm.mileston2.DAO.WalletDao;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.model.Wallet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;


@Service
@Transactional
public class WalletService {

    final static Logger logger =Logger.getLogger(WalletService.class.getName());
    @Autowired
    private WalletDao walletDao;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;


    public  String getUsernameByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return currentUserName;
    }


    // post ->create new wallet .
    // first verify user exist or not with his mobile number
    // if exist then create wallet for that user .
    //otherwise throw exception user does not exist.
    public CustomReturnType createWallet(String mobileNumber){
        try {
            User user = userService.findByMobileNumber(mobileNumber);
            // First get username from token and validity of token
            String requestTokenUserName = getUsernameByToken();

            // check userToken and with his details.
            if (user.getUsername().compareTo(requestTokenUserName) != 0) {
                logger.warn("user has unauthorized token !" + "with token : " + requestTokenUserName);
                return new CustomReturnType("User Unauthorized ,Pls use your Token !", HttpStatus.BAD_REQUEST);
            }
            if (!user.getIsActiveWallet()) {
                Wallet wallet = new Wallet();
                wallet.setWalletId(mobileNumber);
                wallet.setCurrBal(0.0F);
                user.setIsActiveWallet(true);
                user.setWallet(wallet);
                userService.updateUser(user);
                walletDao.saveWallet(wallet);
                logger.info("wallet created :" + wallet);
                return new CustomReturnType("wallet created successfully ", HttpStatus.ACCEPTED);
            }
            else {
                return new CustomReturnType("User has already wallet .", HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e){
            logger.info("wallet can't create "+mobileNumber);
            throw  new BadRequestException("can't create wallet.");
        }
    }

    public Wallet getWalletById(String walletId){
           logger.debug("retrieved wallet by walletId: " + walletId);
            return walletDao.findWalletById(walletId);
    }


   public Wallet DeleteWalletById(String walletId) {
        Wallet w= walletDao.findWalletById(walletId);
        User user= userService.findByMobileNumber(walletId);
        user.setWallet(null);
        walletDao.deleteWallet(w);
       logger.debug("delete wallet by walletId: " + walletId);
        return  w;
   }

    public Wallet updateWallet(Wallet wallet){
        walletDao.saveWallet(wallet);
        logger.debug("update wallet by walletId : " +wallet.getWalletId());
        return wallet;
    }

    public CustomReturnType AddMoney(String mobileNumber,Float amount) {
        try {
            User user = userService.findByMobileNumber(mobileNumber);
            String requestTokenUserName = getUsernameByToken();
           if(amount<=0 || user.getUsername().compareTo(requestTokenUserName) != 0 || !user.getIsActiveWallet())
               return new CustomReturnType("Invalid amount/token or user haven't wallet ",HttpStatus.BAD_REQUEST);

           Wallet wallet=walletDao.findWalletById(mobileNumber);
            wallet.setCurrBal(wallet.getCurrBal() + amount);
            logger.info("amount added in your wallet : "+wallet);
            walletDao.saveWallet(wallet);
           //Done: create new transaction .
            Transaction transaction = new Transaction.TransactionBuilder()
                    .setAmount(amount)
                    .setPayeeWalletId("Self")
                    .setStatus("Success")
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setPayerWalletId(mobileNumber)
                    .build();
            transactionService.createTxn(transaction);
            logger.info("new transaction happened : "+ transaction);
            return new CustomReturnType("money added successfully in your wallet ",HttpStatus.ACCEPTED);
        }
        catch (Exception e){
          throw  new BadRequestException("can't be add money in your wallet");
    }

    }


}
