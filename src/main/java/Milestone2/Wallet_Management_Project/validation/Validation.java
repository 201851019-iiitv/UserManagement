package Milestone2.Wallet_Management_Project.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

     public boolean emailValidation(String email){

         //1) A-Z characters allowed
         //2) a-z characters allowed
         //3) 0-9 numbers allowed
         //4) Additionally email may contain only dot(.), dash(-) and underscore(_)
         //5) Rest all characters are not allowed.
         String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
         Pattern p=Pattern.compile(regex);
         Matcher m = p.matcher(email);

         return m.matches();
     }


     public boolean mobileNumberValidation(String mobileNumber) {

         if(mobileNumber.length()!=10)
             return false;

         // First create a regex b/w 0-9.
         String regex ="[0-9]+";

         //Compile the regex .
         Pattern p=Pattern.compile(regex);

         //Find the match b/w regex and given string
         Matcher m=p.matcher(mobileNumber);

       // return match result.
         return m.matches();

     }

}
