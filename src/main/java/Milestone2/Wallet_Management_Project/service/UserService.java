package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.DAO.UserRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    final static Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepo userRepo;


    @Transactional
    public List<User> getAllUser(){

        return userRepo.findAll();
    }
    @Transactional
    public User createUser(User user){
        logger.debug("created user & userId: "+ user.getUserId());
        return userRepo.save(user) ;
    }

    @Transactional
    public Optional<User> getUserById(Long userId){
        Optional<User> user=  userRepo.findById(userId);

        logger.debug("retrieved user by userId :" + userId);
        return user;
    }

    @Transactional
    public  void updateUser(User user){
        userRepo.save(user);
        logger.debug("update user by userId :" + user.getUserId());
    }

    @Transactional
    public void deleteUser(User user){
        long userId =user.getUserId();
        userRepo.delete(user);
        logger.debug("delete user by userId :" + userId);
    }

    // find by mobile number

   @Transactional
    public User findByMobileno(String mobileNumber) {
       logger.debug("retrieved user by mobileNumber: " + mobileNumber);
        return userRepo.findByMobileno(mobileNumber);
    }

    @Transactional
    public User findByEmail(String email) {
        logger.debug("retrieved user by email: " + email);
        return userRepo.findByEmail(email);
    }


    @Transactional

    public  User findUserByUsername(String username){
        logger.debug("retrieved user by username: " + username);
        return userRepo.findByUsername(username);
    }


    @Transactional
    public User deleteUserByMobileNumber(String mobileNumber) {
        User user=userRepo.findByMobileno(mobileNumber);
        userRepo.delete(user);
        logger.debug("delete user by mobileNumber :" + mobileNumber + " & userId : "+ user.getUserId());
        return  user;
    }
}
