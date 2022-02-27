package com.paytm.mileston2.config;


import com.paytm.mileston2.service.CustomUserDetailsJwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailsJwtService customUserDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }


    //creating the bean used to exists previous version of spring
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


    //ToDo: understands csrf,cor,sessionpolicy stateless.
    //ToDo: handle error and show msg as user unauthorized.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .csrf()
               .disable()
               .cors()
               .disable()
               .authorizeRequests()
               .antMatchers(HttpMethod.PUT ,"/user").authenticated()
               .antMatchers(HttpMethod.DELETE ,"/user").authenticated()
               .antMatchers(HttpMethod.GET,"/wallet/*/txns").permitAll()
               .antMatchers("/wallet/*").authenticated()
               .antMatchers(HttpMethod.POST ,"/transaction").authenticated()
               .anyRequest().permitAll()
               .and()
               .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint);

       http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    //ToDo: change password encoder.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
}
