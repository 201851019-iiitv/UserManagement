package com.paytm.mileston2.controller;

import com.paytm.mileston2.DTO.CustomReturnType;
import com.paytm.mileston2.model.User;
import com.paytm.mileston2.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.mileston2.utilities.FileUtilities;
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
    void createUser() throws Exception {
        String requestJson= FileUtilities.getJsonStringFromFile("userCreateReq.json");
       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/user")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andReturn();

       CustomReturnType actualResponse= (CustomReturnType) FileUtilities.getObjectFromjsonString(result.getResponse().getContentAsString(),CustomReturnType.class);
       CustomReturnType expectedResponse =(CustomReturnType) FileUtilities.getObjectFromFile("userCreateRes.json",CustomReturnType.class);

        Assert.assertEquals(expectedResponse.getMsg(),actualResponse.getMsg()) ;
        Assert.assertEquals(expectedResponse.getStatus(),actualResponse.getStatus());
        //Delete user as well because it is only for testing purpose.
        //get json data for that string .
        String mobileNumber = FileUtilities.getAttributeFromjsonString(requestJson,"mobileNumber");
        userService.deleteUserByMobileNumber(mobileNumber);

    }

    //Work perfectly
    @Test
    void getUserById() throws Exception {
        String userRes= FileUtilities.getJsonStringFromFile("userDetails.json");
        User user= (User) FileUtilities.getObjectFromFile("userDetails.json",User.class);
        String userId= String.valueOf(user.getUserId());
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId" ,userId))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(userRes))
                        .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

        Long userId=Long.parseLong(FileUtilities.getAttributeFromFile("userId.json","userId"));
        User user =userService.getUserById(userId);
        String userToken = token.GenerateMockMvcToken(user.getMobileNumber());

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/user")
                        .param("userId" ,String.valueOf(userId))
                        .header(AUTHORIZATION,"Bearer "+userToken))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andReturn();

        CustomReturnType actualResponse= (CustomReturnType) FileUtilities.getObjectFromjsonString(result.getResponse().getContentAsString(),CustomReturnType.class);
        CustomReturnType expectedResponse =(CustomReturnType) FileUtilities.getObjectFromFile("userDeleteRes.json",CustomReturnType.class);

        Assert.assertEquals(expectedResponse.getMsg(),actualResponse.getMsg());
        Assert.assertEquals(expectedResponse.getStatus(),actualResponse.getStatus());
        //revert back the data
        //issue same id is not need set same id .
        userService.updateUser(user);

    }


    @Test
    void updateUser() throws Exception {
      String userJson= FileUtilities.getJsonStringFromFile("userDetails.json");
        User user = (User) FileUtilities.getObjectFromFile("userDetails.json",User.class);
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