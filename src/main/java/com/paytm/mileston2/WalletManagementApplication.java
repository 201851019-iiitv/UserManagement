package com.paytm.mileston2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Wallet API", description = "Wallet Management Information"))
public class WalletManagementApplication {

	//spring Runner class
	public static void main(String[] args) {
		SpringApplication.run(WalletManagementApplication.class, args);
	}

}
