package com.paytm.mileston2.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.mileston2.DTO.JwtRequest;
import com.paytm.mileston2.DTO.JwtResponse;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Component
public class Token {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;


    public String GenerateMockMvcToken(String mobileNumber) throws Exception {
        User user=userService.findByMobileNumber(mobileNumber);
        // user -->username and password .
        String username=user.getUsername();
        String password=user.getPassword();
        JwtRequest jwtRequest=new JwtRequest(username,password);
        String requestTokenJson =objectMapper.writeValueAsString(jwtRequest);

        //GenerateToken for user.
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/GenerateTokens")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestTokenJson))
                .andExpect( MockMvcResultMatchers.status().isOk())
                .andReturn();
        String resultContent=result.getResponse().getContentAsString();
        JwtResponse token=objectMapper.readValue(resultContent,JwtResponse.class);

        String userToken =token.getToken();

        return userToken;
    }
}
