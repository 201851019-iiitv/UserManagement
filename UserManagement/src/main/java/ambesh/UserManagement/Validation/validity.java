package ambesh.UserManagement.Validation;

import ambesh.UserManagement.model.User;
import ambesh.UserManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class validity {


    @Autowired
    private UserRepository userRepository;


    // check user email exist or not .
    public boolean doesEmailIDExist(String emailID){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if(userlist.get(i).getEmailID().equals(emailID));
            return true;
        }
        return false;
    }

    // check user mobile exist or not .
    public boolean doesMobileExist(String mobile){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if(userlist.get(i).getMobileNumber().equals(mobile))
                return true;
        }
        return false;
    }
    // check user userName exist or not .
    public boolean doesUserExist(String userName){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if(userlist.get(i).getUserName().equals(userName));
            return true;
        }
        return false;
    }

    // check user mobile number,username,email exist or not .
    public boolean doesExist(User user){
        List<User> userlist= (List<User>)userRepository.findAll();
        // check emailId have same ;
        for(int i=0 ; i < userlist.size() ; i++) {
            if((userlist.get(i).getUserName().equals(user.getUserName())) ||(userlist.get(i).getEmailID().equals(user.getEmailID()))||(userlist.get(i).getMobileNumber().equals(user.getMobileNumber())))
                return true;
        }
        return false;
    }


    public String validation(User user){
        if(user.getUserName()==null )
            return "UserName";
        else if(user.getFirstName()==null || user.getLastName()==null )
            return "first name or last name";
        else if(user.getMobileNumber()==null || user.getMobileNumber().length()!=10)
            return "Mobile number";
        else if(user.getEmailID()==null || !user.getEmailID().contains("@"))
            return "email Id";
        else if(user.getAddress1()==null && user.getAddress2()==null)
            return "Address";
        else
            return "completed";

    }

}
