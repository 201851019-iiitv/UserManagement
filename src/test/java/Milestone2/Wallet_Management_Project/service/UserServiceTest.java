package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.DAO.UserRepo;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @MockBean
   private UserRepo userRepo;

    @InjectMocks
   private UserService userService;

    @Test
    void createUser() {
        User user =new User();
        user.setName("ambesh");
        user.setMobileno("1234567890");
        user.setUsername("ambesh11");
        user.setPassword("2442");
        user.setEmail("am@121");
        Mockito.when(userRepo.save(user)).thenReturn(user);
        Assert.assertEquals(userService.createUser(user),user);

    }

    @Test
    void findByMobileno() {
        String mobileNumber="1234567890";
        User user =new User();
        user.setName("ambesh");
        user.setMobileno(mobileNumber);
        user.setUsername("ambesh11");
        user.setPassword("2442");
        user.setEmail("am@121");
        Mockito.when(userRepo.findByMobileno(mobileNumber)).thenReturn(user);
        Assert.assertEquals(userService.findByMobileno(mobileNumber),user);
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findUserByUsername() {
    }
}