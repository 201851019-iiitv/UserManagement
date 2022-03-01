package com.paytm.mileston2.DAO;

import com.paytm.mileston2.DAO.Impl.IUserDao;
import com.paytm.mileston2.Repository.UserRepo;
import com.paytm.mileston2.exception.ResourceNotFoundException;
import com.paytm.mileston2.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDao implements IUserDao {

    @Autowired
   private UserRepo userRepo;


    @Override
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }


    @Override
    public User saveUser(User user) {
         userRepo.save(user);
         return user;
    }

    @Override
    public User findUserById(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found ."));
        return user;
    }

    @Override
    public User deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found ."));
        userRepo.delete(user);
     return  user;
    }

    @Override
    public User findByMobileNumber(String mobileNumber) {
        try {
            User user = userRepo.findByMobileNumber(mobileNumber);
            return user;
        }
        catch(Exception e){
            throw new ResourceNotFoundException("user not exist.");
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            User user = userRepo.findByEmail(email);
            return user;
        }
        catch (Exception e) {
            throw new ResourceNotFoundException("user not exist.");
        }
    }

    @Override
    public User findByUsername(String username) {
        try {
            User user = userRepo.findByUsername(username);
            return user;
        }
        catch (Exception e){
            throw new ResourceNotFoundException("user not exist.");
        }
    }
}
