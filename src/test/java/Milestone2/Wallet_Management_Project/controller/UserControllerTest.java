package Milestone2.Wallet_Management_Project.controller;

import Milestone2.Wallet_Management_Project.DTO.CustomReturnType;
import Milestone2.Wallet_Management_Project.DTO.JwtRequest;
import Milestone2.Wallet_Management_Project.DTO.JwtResponse;
import Milestone2.Wallet_Management_Project.exception.BadRequestException;
import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {



    @Autowired
    MockMvc mockMvc;


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserService userService;

    // for generate token .
    public  String  GenerateMockMvcToken(User user) throws Exception {
        // user -->username and password .
        String username=user.getUsername();
        String password=user.getPassword();

        JwtRequest jwtRequest=new JwtRequest(username,password);

        String requestTokenJson =objectMapper.writeValueAsString(jwtRequest);

        //GenrateToken for user.

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



    //Done work Perfectly
    @Test
    void createSuccessfulUser() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserCreateReq.json")));
        String ExpectedOutputJson = new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserCreateRes.json")));

       MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/user")
                       .contentType(MediaType.APPLICATION_JSON_VALUE)
                       .content(requestJson))
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andReturn();
       String resultContent=result.getResponse().getContentAsString();
       CustomReturnType response=objectMapper.readValue(resultContent, CustomReturnType.class);
       CustomReturnType expectedResponse = objectMapper.readValue(ExpectedOutputJson ,CustomReturnType.class);
        Assert.assertEquals(expectedResponse.getMsg(),response.getMsg()) ;
        Assert.assertEquals(expectedResponse.getStatus() ,response.getStatus());

        //Delete user as well because it is only for testing purpose.
        //get json data for that string .
        JSONObject jsonObject = new JSONObject(requestJson);
        String mobileNumber = jsonObject.getString("mobileno");
        userService.deleteUserByMobileNumber(mobileNumber);

    }

    //Work perfectly
    @Test
    void getUserById() throws Exception {

        String userId="3";
        String userRes=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDetails.json")));
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/user")
                        .param("userId" ,userId))
                        .andExpect( MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.content().json(userRes))
                        .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

       Long userId=17L;
        //Store already user so we can back again data .
        User user=userService.getUserById(userId).orElseThrow(()->new BadRequestException("user Id not found !"));

        //generate token
        String userToken = GenerateMockMvcToken(user);

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/user")
                        .param("userId" ,String.valueOf(userId))
                        .header(AUTHORIZATION,"Bearer "+userToken))
                .andExpect( MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        // Response class .
        String walletResJson=new String(Files.readAllBytes(Paths.get("src/test/java/DTO/UserDeleteRes.json")));
        CustomReturnType resMsg =objectMapper.readValue(walletResJson, CustomReturnType.class);


        Assert.assertEquals(resMsg.getMsg() ,msg.getMsg());
        Assert.assertEquals(resMsg.getStatus(),msg.getStatus());

        //revert back the data
        userService.updateUser(user);

    }



}