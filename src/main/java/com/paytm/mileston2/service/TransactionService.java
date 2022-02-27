package com.paytm.mileston2.service;

import com.paytm.mileston2.DAO.TransactionDao;
import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.exception.BadRequestException;
import com.paytm.mileston2.model.Transaction;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.model.Wallet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;


@Service
@Transactional
public class TransactionService {

   final static Logger  logger =Logger.getLogger(TransactionService.class.getName());

    @Autowired
    TransactionDao transactionDao;
    @Autowired
    UserService userService;
    @Autowired
    WalletService walletService;

    public  String getUsernameByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        return currentUserName;
    }

    // This Function create new Transaction.
    public Transaction createTxn(Transaction txn) {

         logger.debug("created transaction & txnId: "+ txn.getTxnId());
          transactionDao.saveTxn(txn);

          return txn;
    }


    // This Function Returns All Transaction made by a user either it's as Payer or Payee.
    public Page<Transaction> getAllTxnsByWalletId(String walletId ,int pageNo) {


        Pageable pageable = PageRequest.of(pageNo,2);
        Page<Transaction> txns = transactionDao.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable);
        logger.debug("retrieved all transaction by wallet id by per 2 transaction at each page ! & walletId: "+walletId +"&pageNo: "+pageNo);
        return txns;
    }

    public Page<Transaction> getAllTxnsByUserId(Long userId,int pageNo) {
        User user = userService.getUserById(userId);
        Wallet wallet= walletService.getWalletById(user.getMobileNumber());
        Page<Transaction> txns = getAllTxnsByWalletId(wallet.getWalletId(), pageNo);
        logger.info("retrieved all transaction by wallet id by per 2 transaction at each page ! & wallet: "+wallet +"&pageNo: "+pageNo);
        return txns;
    }

    // get The Transaction details by it's TxnId.
    public Transaction getTxnsByTxnId(Long txnId) {
        logger.debug("retrieved all transaction by txn id and txnId : " +txnId);
        return transactionDao.findTxnByTxnId(txnId);
    }

    public CustomReturnType TransferMoney(Transaction txns){


        User user = userService.findByMobileNumber(txns.getPayerWalletId());
        // First get username from token and validity of token
        String requestTokenUserName=getUsernameByToken();

        // check userToken and with his details.
        if(user.getUsername().compareTo(requestTokenUserName)!=0) {
            logger.error("user unauthorized token !" +"with token : "+requestTokenUserName);
            return new CustomReturnType("User Unauthorized ,Pls use your Token !", HttpStatus.BAD_REQUEST);
        }

      Wallet payerWallet = walletService.getWalletById(txns.getPayerWalletId());
      Wallet payeeWallet = walletService.getWalletById(txns.getPayeeWalletId());

        if(txns.getAmount()<=0 || payeeWallet.getCurrBal()< txns.getAmount())
            return new CustomReturnType("Invalid amount", HttpStatus.BAD_REQUEST);

        // it create temp replication data when server failed in b\w execution.
        Wallet TempPayerWallet=payerWallet;
        Wallet TempPayeeWallet=payeeWallet;

        try{
            payerWallet.setCurrBal(payerWallet.getCurrBal()- txns.getAmount());
            payeeWallet.setCurrBal(payerWallet.getCurrBal()+ txns.getAmount());
            walletService.updateWallet(payeeWallet);
            walletService.updateWallet(payerWallet);
            //Done: create new transaction .
            Transaction transaction = new Transaction.TransactionBuilder()
                    .setAmount(txns.getAmount())
                    .setPayeeWalletId(payeeWallet.getWalletId())
                    .setStatus("Success")
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setPayerWalletId(payerWallet.getWalletId())
                    .build();
            transactionDao.saveTxn(transaction);
            logger.info("new transaction happened : "+ transaction);

            return new CustomReturnType("money transfer successfully with transtion",HttpStatus.ACCEPTED);

        }
        catch (Exception e){
            logger.warn("transaction failed " + txns);
            walletService.updateWallet(TempPayeeWallet);
            walletService.updateWallet(TempPayerWallet);
            Transaction transaction = new Transaction.TransactionBuilder()
                    .setAmount(txns.getAmount())
                    .setPayeeWalletId(payeeWallet.getWalletId())
                    .setStatus("Failed")
                    .setTimestamp(Timestamp.from(Instant.now()))
                    .setPayerWalletId(payerWallet.getWalletId())
                    .build();
            transactionDao.saveTxn(transaction);
            return new CustomReturnType("Transaction Failed ",HttpStatus.BAD_REQUEST);
        }



    }



}
