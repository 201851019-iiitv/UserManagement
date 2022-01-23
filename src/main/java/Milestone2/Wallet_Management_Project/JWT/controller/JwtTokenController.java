package Milestone2.Wallet_Management_Project.JWT.controller;


import Milestone2.Wallet_Management_Project.JWT.helper.JwtResponse;
import Milestone2.Wallet_Management_Project.JWT.helper.JwtUtil;
import Milestone2.Wallet_Management_Project.JWT.model.JwtRequest;
import Milestone2.Wallet_Management_Project.JWT.service.CustomUserDetailsService;
import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
public class JwtTokenController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(path="/GenerateTokens",method = RequestMethod.POST)
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){
     try{
         this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));

     }
     catch (Exception e) {
         throw new BadRequestException("Bad credentials !");
     }



        UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
       String token =this.jwtUtil.generateToken(userDetails);

       return ResponseEntity.ok(new JwtResponse(token));
    }
}
