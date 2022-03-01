package com.paytm.mileston2.controller.VerifyRequestAndToken;

import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class verifyRequestAndToken {

    @Autowired
    UserService userService;


    public  String getUsernameByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        return currentUserName;
    }

    public String getUsername(String mobileNumber){
        User user =userService.findByMobileNumber(mobileNumber);

        return user.getUsername();
    }

    public boolean  validateToken(String mobileNumber){

        String RequestTokenUsername= getUsernameByToken();
        String RequestUrlUsername=getUsername(mobileNumber);

        return RequestTokenUsername.compareTo(RequestUrlUsername)==0? true:false;

    }



}
