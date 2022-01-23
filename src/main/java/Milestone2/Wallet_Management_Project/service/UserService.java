package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    @Transactional
    public List<User> getAllUser(){

        return userRepo.findAll();
    }
    @Transactional
    public boolean createUser(User user){

        return userRepo.save(user) !=null;
    }

    @Transactional
    public Optional<User> getUserById(Long userId){
        Optional<User> user=  userRepo.findById(userId);


        return user;
    }

    @Transactional
    public  void updateUser(User user){
        userRepo.save(user);
    }

    @Transactional
    public void deleteUser(User user){
        userRepo.delete(user);

    }

    // find by mobile number

   @Transactional
    public User findByMobileno(String mobileNumber) {
        return userRepo.findByMobileno(mobileNumber);
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }


    @Transactional

    public  User findUserByUsername(String username){
        return userRepo.findByUsername(username);
    }
}
