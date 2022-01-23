package Milestone2.Wallet_Management_Project.JWT.service;

import Milestone2.Wallet_Management_Project.exception.ResourceNotFoundException;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

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
