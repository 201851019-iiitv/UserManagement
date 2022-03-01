package com.paytm.mileston2.Repository;

import com.paytm.mileston2.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepo extends JpaRepository<Transaction ,Long> {
   Transaction findByTxnId(Long txnId);
   Page<Transaction> findByPayerWalletIdOrPayeeWalletId(String PayerWalletId, String PayeeWalletId, Pageable pageable);

}