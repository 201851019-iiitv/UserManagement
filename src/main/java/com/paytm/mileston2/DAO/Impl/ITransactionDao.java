package com.paytm.mileston2.DAO.Impl;

import com.paytm.mileston2.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransactionDao {

    void saveTxn(Transaction txn);
    Page<Transaction> findByPayerWalletIdOrPayeeWalletId(String walletId, String walletId1, Pageable pageable);
    Transaction findTxnByTxnId(Long txnId);
}
