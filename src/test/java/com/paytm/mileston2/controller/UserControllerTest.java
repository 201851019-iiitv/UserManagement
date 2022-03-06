package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.mileston2.utilities.TestUtility;
import com.paytm.mileston2.utilities.Token;
import org.json.JSONObject;
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
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

   @Autowired
   private Token token;


    //Done work Perfectly
    @Test
    void createSuccessfulUser() throws Exception {
        String requestJson= TestUtility.getJsonStringFromFile("UserCreateReq.json");
       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/user")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andReturn();

       CustomReturnType response= (CustomReturnType) TestUtility.getObjectFromFile(result.getResponse().getContentAsString(),CustomReturnType.class);
        Assert.assertEquals(response.getMsg(),"User created successfully.") ;
        //Delete user as well because it is only for testing purpose.
        //get json data for that string .
        JSONObject jsonObject = new JSONObject(requestJson);
        String mobileNumber = jsonObject.getString("mobileNumber");
        userService.deleteUserByMobileNumber(mobileNumber);

    }

    //Work perfectly
    @Test
    void getUserById() throws Exception {
        String userRes= TestUtility.getJsonStringFromFile("UserDetails.json");
        User user= (User) TestUtility.getObjectFromFile("UserDetails.json",User.class);
        String userId= String.valueOf(user.getUserId());
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId" ,userId))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(userRes))
                        .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

       Long userId=7L;
       User user =userService.getUserById(userId);
        String userToken = token.GenerateMockMvcToken(user.getMobileNumber());

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/user")
                        .param("userId" ,String.valueOf(userId))
                        .header(AUTHORIZATION,"Bearer "+userToken))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        Assert.assertEquals(msg.getMsg(),"User delete successfully");

        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);

    }


    @Test
    void updateUser() throws Exception {
      String userJson=TestUtility.getJsonStringFromFile("UserDetails.json");
        User user = (User) TestUtility.getObjectFromFile("UserDetails.json",User.class);
        String userToken = token.GenerateMockMvcToken(user.getMobileNumber());


        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.put("/user")
                        .header(AUTHORIZATION,"Bearer "+userToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(userJson))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        Assert.assertEquals(msg.getMsg(),"User updated successfully.");

        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);



    }

}