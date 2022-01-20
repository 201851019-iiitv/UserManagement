package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Transaction;
import Milestone2.Wallet_Management_Project.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
    public Page<Transaction> getAllTxnsByWalletId(String walletId ,int pageNo) {


        Pageable pageable = PageRequest.of(pageNo,5);
        Page<Transaction> txns = transactionRepo.findByPayerWalletIdOrPayeeWalletId(walletId,walletId,pageable);
        return txns;
    }


    // Get The Transaction details by it's TxnId.

    public Transaction getTxnsByTxnId(Long txnId) {
        return transactionRepo.findByTxnId(txnId);
    }




}
