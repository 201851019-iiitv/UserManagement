package Milestone2.Wallet_Management_Project.repository;

import Milestone2.Wallet_Management_Project.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface WalletRepo extends JpaRepository<Wallet ,String> {
}
