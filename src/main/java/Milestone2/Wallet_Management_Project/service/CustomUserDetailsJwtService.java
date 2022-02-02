package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsJwtService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try{

            User user=userService.findUserByUsername(username);

            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),new ArrayList<>());

        }
        catch (Exception e){
            throw new ResourceNotFoundException("Username is invalid ,Pls enter correct username !");
        }



    }
}
