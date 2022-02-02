package Milestone2.Wallet_Management_Project.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
//this class help to taken username and password in json
public class JwtRequest {

    private String username;
    private String password;
}
