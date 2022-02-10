package Milestone2.Wallet_Management_Project;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Wallet API", description = "Wallet Management Information"))
public class WalletManagementApplication {

	//spring Runner class
	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(WalletManagementApplication.class, args);
	}

}
