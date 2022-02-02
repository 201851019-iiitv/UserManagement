package Milestone2.Wallet_Management_Project.DAO;

import Milestone2.Wallet_Management_Project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {

    User findByMobileno(String mobileno);
    User findByEmail(String email);
    User findByUsername(String username);

}
