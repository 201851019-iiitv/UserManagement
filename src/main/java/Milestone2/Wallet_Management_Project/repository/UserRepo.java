package Milestone2.Wallet_Management_Project.repository;

import Milestone2.Wallet_Management_Project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByMobileno(String mobileno);

}