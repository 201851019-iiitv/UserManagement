package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.DAO.TransactionRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class TransactionService {

   final static Logger  logger =Logger.getLogger(TransactionService.class.getName());
    @Autowired
    TransactionRepo transactionRepo;

    // This Function create new Transaction.
    public Transaction createTxn(Transaction txn) {

     logger.debug("created transaction & txnId: "+ txn.getTxnId());
          transactionRepo.save(txn);

          return txn;
    }


    // This Function Returns All Transaction made by a user either it's as Payer or Payee.
    public Page<Transaction> getAllTxnsByWalletId(String walletId ,int pageNo) {


        Pageable pageable = PageRequest.of(pageNo,2);
        Page<Transaction> txns = transactionRepo.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable);
        logger.debug("retrieved all transaction by wallet id by per 2 transaction at each page ! & walletId: "+walletId +"&pageNo: "+pageNo);
        return txns;
    }


    // Get The Transaction details by it's TxnId.

    public Transaction getTxnsByTxnId(Long txnId) {
        logger.debug("retrieved all transaction by txn id and txnId : " +txnId);
        return transactionRepo.findByTxnId(txnId);
    }




}
