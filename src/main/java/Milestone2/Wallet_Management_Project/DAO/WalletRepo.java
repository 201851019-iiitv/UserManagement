package Milestone2.Wallet_Management_Project.DAO;

import Milestone2.Wallet_Management_Project.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;



public interface WalletRepo extends JpaRepository<Wallet ,String> {
}
