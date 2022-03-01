package com.paytm.mileston2.DAO;

import com.paytm.mileston2.DAO.Impl.ITransactionDao;
import com.paytm.mileston2.Repository.TransactionRepo;
import com.paytm.mileston2.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class TransactionDao implements ITransactionDao {


    @Autowired
    private TransactionRepo transactionRepo;

    @Override
    public Transaction saveTxn(Transaction txn) {
        transactionRepo.save(txn);
        return txn;
    }

    @Override
    public Page<Transaction> findByPayerWalletIdOrPayeeWalletId(String walletId, String walletId1, Pageable pageable) {
        return transactionRepo.findByPayerWalletIdOrPayeeWalletId(walletId,walletId1,pageable);
    }

    @Override
    public Transaction findTxnByTxnId(Long txnId) {
        return transactionRepo.findByTxnId(txnId);
    }
}
