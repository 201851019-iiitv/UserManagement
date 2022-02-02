package Milestone2.Wallet_Management_Project.config;

import Milestone2.Wallet_Management_Project.utilities.Jwt.JwtUtil;
import Milestone2.Wallet_Management_Project.service.CustomUserDetailsJwtService;
import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsJwtService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Get header and condition like
        // start with bearer and validate the tokens.

        String authToken=request.getHeader("Authorization");
        String username ,jwtToken;

        //Auth token is not null and start with Bearer check condition;
        if(authToken!=null && authToken.startsWith("Bearer ")){
            jwtToken=authToken.substring(7); // beacuse "Bearer "  -> len=7 & index with 0;

            try{

              username=jwtUtil.extractUsername(jwtToken);

              //username will not be null and
              //ToDo: I was not able to understand that "whole if condition "

              if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                  UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                  usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
              }

              else{
                  throw  new BadRequestException("invalid token !");

              }
            }
            catch (Exception e){
                throw  new BadRequestException("invalid token !");
            }

         }

        filterChain.doFilter(request,response);
    }
}
