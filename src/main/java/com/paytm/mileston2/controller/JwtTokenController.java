package com.paytm.mileston2.controller;


import com.paytm.mileston2.DTO.JwtResponse;
import com.paytm.mileston2.utilities.JwtUtil;
import com.paytm.mileston2.DTO.JwtRequest;
import com.paytm.mileston2.service.CustomUserDetailsJwtService;
import com.paytm.mileston2.exception.BadRequestException;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtTokenController {

    final static Logger logger = Logger.getLogger(JwtTokenController.class.getName());

    @Autowired
    private CustomUserDetailsJwtService customUserDetailsService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/GenerateTokens")
    @Operation(summary = "This method use to generate JWT token for user",description="to get token .Required username and password")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){
     try{
         this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));

     }
     catch (Exception e) {
         logger.error("Bad credentials !"+" username :" + jwtRequest.getUsername() +" password : "+ jwtRequest.getPassword());
         throw new BadRequestException("Bad credentials !");
     }



        UserDetails userDetails=this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
       String token =this.jwtUtil.generateToken(userDetails);
      logger.info(" username :" + jwtRequest.getUsername() +" password : "+ jwtRequest.getPassword());
      logger.info("Token generated :" + token);
       return ResponseEntity.ok(new JwtResponse(token));
    }
}
