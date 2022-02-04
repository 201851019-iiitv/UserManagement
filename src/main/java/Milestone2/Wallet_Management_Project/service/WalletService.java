package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.DAO.WalletRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Wallet createWallet(Wallet wallet){

        walletRepo.save(wallet);
        return wallet;

    }

    public Optional<Wallet> getWalletById(String walletId){
            return walletRepo.findById(walletId);
    }




    public Wallet updateWallet(Wallet wallet){
        walletRepo.save(wallet);
        return wallet;
    }


}

