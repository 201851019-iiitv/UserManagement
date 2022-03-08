package com.paytm.mileston2.controller;

import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import com.paytm.mileston2.utilities.FileUtilities;
import com.paytm.mileston2.utilities.ResultMatcher;
import com.paytm.mileston2.utilities.Token;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private Token token;


    //Done work Perfectly
    @Test
    void createSuccessfullyUser() throws Exception {
        String requestJson = FileUtilities.getJsonStringFromFile("userSuccessCreateReq.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.isMatched(result.getResponse().getContentAsString(),"userCreateRes.json");
        Assert.assertTrue("User can't created user ",output); // if output is false then print message .

        String mobileNumber = FileUtilities.getAttributeFromjsonString(requestJson, "mobileNumber");
        userService.deleteUserByMobileNumber(mobileNumber);

    }

    //Done work Perfectly
    @Test
    void createUserWithInvalidConstraint() throws Exception {
        String requestJson = FileUtilities.getJsonStringFromFile("creatUnsuccessful.json");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.Matched(result.getResponse().getContentAsString(),"createInvalidUser.json");
        Assert.assertTrue("User can't created user ",output); // if output is false then print message .
    }

    //Work perfectly
    @Test
    void getUserById() throws Exception {
        String userRes = FileUtilities.getJsonStringFromFile("userDetails.json");
        User user = (User) FileUtilities.getObjectFromFile("userDetails.json", User.class);
        String userId = String.valueOf(user.getUserId());
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userRes))
                .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

        Long userId = Long.parseLong(FileUtilities.getAttributeFromFile("userId.json", "userId"));
        User user = userService.getUserById(userId);
        String userToken = token.GenerateMockMvcToken(user.getMobileNumber());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/user")
                        .param("userId", String.valueOf(userId))
                        .header(AUTHORIZATION, "Bearer " + userToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.isMatched(result.getResponse().getContentAsString(),"userDeleteRes.json");
        Assert.assertTrue("User can't deleted",output);
        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);

    }


    @Test
    void updateUser() throws Exception {
        String userJson = FileUtilities.getJsonStringFromFile("userDetails.json");
        User user = (User) FileUtilities.getObjectFromFile("userDetails.json", User.class);
        String userToken = token.GenerateMockMvcToken(user.getMobileNumber());


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .header(AUTHORIZATION, "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean output= ResultMatcher.isMatched(result.getResponse().getContentAsString(),"userUpdateRes.json");
        Assert.assertTrue("User can't update",output);
        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);

    }

}