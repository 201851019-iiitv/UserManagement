package Milestone2.Wallet_Management_Project;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalletManagementApplication {

	//spring Runner class
	public static void main(String[] args) {
		BasicConfigurator.configure();
		SpringApplication.run(WalletManagementApplication.class, args);
	}

}
