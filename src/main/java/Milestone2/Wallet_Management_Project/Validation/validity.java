package Milestone2.Wallet_Management_Project.Validation;


import Milestone2.Wallet_Management_Project.model.User;
import Milestone2.Wallet_Management_Project.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class validity {


    @Autowired
    private UserRepo userRepository;


    // check user email exist or not .
    public boolean doesEmailIDExist(String emailID){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if(userlist.get(i).getEmail().equals(emailID));
            return true;
        }
        return false;
    }

    // check user mobile exist or not .
    public boolean doesMobileExist(String mobile){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if(userlist.get(i).getMobileno().equals(mobile))
                return true;
        }
        return false;
    }

    // check user mobile number,username,email exist or not .
    public boolean doesExist(User user){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if((userlist.get(i).getEmail().equals(user.getEmail()))||(userlist.get(i).getMobileno().equals(user.getMobileno())))
                return true;
        }
        return false;
    }




}
