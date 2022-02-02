package Milestone2.Wallet_Management_Project.DAO;

import Milestone2.Wallet_Management_Project.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepo extends JpaRepository<Transaction ,Long> {
   Transaction findByTxnId(Long txnId);
   Page<Transaction> findByPayerWalletIdOrPayeeWalletId(String PayerWalletId, String PayeeWalletId, Pageable pageable);

}
