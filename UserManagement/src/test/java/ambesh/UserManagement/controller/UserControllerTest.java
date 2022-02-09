package ambesh.UserManagement.controller;

import ambesh.UserManagement.exception.ResourceNotFoundException;
import ambesh.UserManagement.model.User;
import ambesh.UserManagement.service.UserService;
import ambesh.UserManagement.utilities.CustomReturnType;
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

    //Done work Perfectly
    @Test
    void createSuccessfulUser() throws Exception {

        String requestJson =new String(Files.readAllBytes(Paths.get("src/test/java/ambesh/UserManagement/DTO/UserCreateReq.json")));
        String ExpectedOutputJson = new String(Files.readAllBytes(Paths.get("src/test/java/ambesh/UserManagement/DTO/UserCreateRes.json")));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.post("/api/user")
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
        String userRes=new String(Files.readAllBytes(Paths.get("src/test/java/ambesh/UserManagement/DTO/GetUserRes.json")));
        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/api/user")
                        .param("userId" ,userId))
                .andExpect( MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(userRes))
                .andReturn();

    }


    @Test
    void deleteUser() throws Exception {

        Long userId=3L;
        //Store already user so we can back again data .
        User user=userService.getUserById(userId).orElseThrow(()->new ResourceNotFoundException("user Id not found !"));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.delete("/api/user")
                        .param("userId" ,String.valueOf(userId)))
                .andExpect( MockMvcResultMatchers.status().isOk())
                .andReturn();

        String response =result.getResponse().getContentAsString();
        CustomReturnType msg =objectMapper.readValue(response, CustomReturnType.class);

        // Response class .
        String walletResJson=new String(Files.readAllBytes(Paths.get("src/test/java/ambesh/UserManagement/DTO/UserDeleteRes.json")));
        CustomReturnType resMsg =objectMapper.readValue(walletResJson, CustomReturnType.class);


        Assert.assertEquals(resMsg.getMsg() ,msg.getMsg());
        Assert.assertEquals(resMsg.getStatus(),msg.getStatus());

        //revert back the data
        userService.updateUser(user);

    }



}