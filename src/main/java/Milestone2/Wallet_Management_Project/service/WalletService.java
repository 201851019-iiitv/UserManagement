package Milestone2.Wallet_Management_Project.service;

import Milestone2.Wallet_Management_Project.model.Wallet;
import Milestone2.Wallet_Management_Project.DAO.WalletRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class WalletService {

    final static Logger logger =Logger.getLogger(WalletService.class.getName());
    @Autowired
    private WalletRepo walletRepo;

    // post ->create new wallet .
    // first verify user exist or not with his mobile number
    // if exist then create wallet for that user .
    //otherwise throw exception user does not exist.
    public Wallet createWallet(Wallet wallet){

        logger.debug("wallet created with walletId : " + wallet.getWalletId());
        walletRepo.save(wallet);
        return wallet;

    }

    public Optional<Wallet> getWalletById(String walletId){
        logger.debug("retrieved wallet by walletId: " + walletId);
            return walletRepo.findById(walletId);
    }


   public Wallet DeleteWalletById(String walletId) {
        Wallet w=walletRepo.getById(walletId);
        walletRepo.delete(w);
       logger.debug("delete wallet by walletId: " + walletId);
        return  w;
   }

    public Wallet updateWallet(Wallet wallet){
        walletRepo.save(wallet);
        logger.debug("update wallet by walletId : " +wallet.getWalletId());
        return wallet;
    }


}

