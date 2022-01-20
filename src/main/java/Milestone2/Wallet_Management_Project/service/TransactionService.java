package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    @Autowired
    TransactionRepo transactionRepo;

    // This Function create new Transaction.
    public void createTxn(Transaction txn) {

          transactionRepo.save(txn);
    }


    // This Function Returns All Transaction made by a user either it's as Payer or Payee.
    public List<Transaction> getAllTxnsByWalletId(String walletId) {

        List<Transaction>TotalTxns=new ArrayList<>();

        TotalTxns.addAll(transactionRepo.findByPayerWalletId(walletId)); // This will add all txns made as payer.
        TotalTxns.addAll(transactionRepo.findByPayeeWalletId(walletId)); // This will add all txns get as payee.
        return TotalTxns;
    }


    // Get The Transaction details by it's TxnId.

    public Transaction getTxnsByTxnId(Long txnId) {

        return transactionRepo.findByTxnId(txnId);
    }




}
