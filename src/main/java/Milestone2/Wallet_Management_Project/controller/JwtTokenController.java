package Milestone2.Wallet_Management_Project.controller;


import Milestone2.Wallet_Management_Project.DTO.JwtResponse;
import Milestone2.Wallet_Management_Project.utilities.Jwt.JwtUtil;
import Milestone2.Wallet_Management_Project.DTO.JwtRequest;
import Milestone2.Wallet_Management_Project.service.CustomUserDetailsJwtService;
import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtTokenController {

    @Autowired
    private CustomUserDetailsJwtService customUserDetailsService;


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/GenerateTokens")
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
