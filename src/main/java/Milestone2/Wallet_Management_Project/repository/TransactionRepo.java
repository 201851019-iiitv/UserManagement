package Milestone2.Wallet_Management_Project.repository;

import Milestone2.Wallet_Management_Project.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface TransactionRepo extends JpaRepository<Transaction ,Long> {
   Transaction findByTxnId(Long txnId);
   List<Transaction> findByPayerWalletId(String PayerWalletId);
   List<Transaction> findByPayeeWalletId(String PayeeWalletId);
}
