package com.paytm.mileston2.DTO;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//this class help to taken username and password in json
public class JwtRequest {

    private String username;
    private String password;
}
