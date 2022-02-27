package com.paytm.mileston2.config;

import com.paytm.mileston2.exception.GlobalException;
import com.paytm.mileston2.utilities.Jwt.JwtUtil;
import com.paytm.mileston2.service.CustomUserDetailsJwtService;
import com.paytm.mileston2.exception.BadRequestException;
import org.apache.log4j.Logger;
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
    final static  Logger logger = Logger.getLogger(JwtAuthFilter.class.getName());

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsJwtService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //Get header and condition like
        // start with bearer and validate the tokens.

        String authToken=request.getHeader("Authorization");
        String methodUrl= request.getRequestURI();
        String username ,jwtToken;

        //Auth token is not null and start with Bearer check condition;
        if(authToken!=null && authToken.startsWith("Bearer ")){
            jwtToken=authToken.substring(7); // beacuse "Bearer "  -> len=7 & index with 0;

            try{

              username=jwtUtil.extractUsername(jwtToken);

              //username will not be null and
              //ToDo: I was not able to understand that "whole if condition "

              if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                  logger.debug("user login successfully from dofilterInternal ! ");
                  logger.info("username : "+username);
                  UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
                  UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                  usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                  SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
              }

              else{
                   throw new BadRequestException("invalid token11 !");

              }
            }
             catch (Exception e){
                 new GlobalException().BadRequest( new BadRequestException("invalid token !"));
            }

         }

        filterChain.doFilter(request,response);
    }
}
