package com.paytm.mileston2.DAO;

import com.paytm.mileston2.DAO.Impl.IWalletDao;
import com.paytm.mileston2.Repository.WalletRepo;
import com.paytm.mileston2.exception.ResourceNotFoundException;
import com.paytm.mileston2.model.Wallet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class WalletDao implements IWalletDao {

    Logger logger =Logger.getLogger(WalletDao.class.getSimpleName());
    @Autowired
    private WalletRepo walletRepo;


    @Override
    public Wallet saveWallet(Wallet wallet) {
        logger.debug("save wallet");
        walletRepo.save(wallet);
        return wallet;
    }

    @Override
    public Wallet findWalletById(String walletId) {
        Wallet wallet = walletRepo.findById(walletId).orElseThrow(()->new ResourceNotFoundException("wallet not found ."));
        return  wallet;
    }


    @Override
    public Wallet deleteWallet(Wallet w) {
        walletRepo.delete(w);
        return w;
    }
}
