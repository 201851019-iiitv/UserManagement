package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.DAO.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    @Autowired
    private WalletRepo walletRepo;

    // post ->create new wallet .
    // first verify user exist or not with his mobile number
    // if exist then create wallet for that user .
    //otherwise throw exception user does not exist.
    public ResponseEntity<?> createWallet(Wallet wallet){

        walletRepo.save(wallet);
        return new ResponseEntity<>("wallet created successfully" , HttpStatus.CREATED);

    }

    public Optional<Wallet> getWalletById(String walletId){
            return walletRepo.findById(walletId);
    }




    public void updateWallet(Wallet wallet){
        walletRepo.save(wallet);
    }


}

