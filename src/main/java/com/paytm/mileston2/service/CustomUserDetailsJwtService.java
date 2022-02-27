package com.paytm.mileston2.service;

import com.paytm.mileston2.exception.ResourceNotFoundException;
import com.paytm.mileston2.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsJwtService implements UserDetailsService {

    final static Logger logger = Logger.getLogger(CustomUserDetailsJwtService.class.getName());

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try{

            User user=userService.findUserByUsername(username);
            logger.debug("load by username !");
            logger.info("username :" + username);
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),new ArrayList<>());

        }
        catch (Exception e){
            throw new ResourceNotFoundException("Username is invalid ,Pls enter correct username !");
        }



    }
}
