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
    public void deleteUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found ."));
        userRepo.delete(user);

    }

    @Override
    public User findByMobileNumber(String mobileNumber) {
        User user = userRepo.findByMobileNumber(mobileNumber);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = userRepo.findByEmail(email);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);
        return user;
    }
}
