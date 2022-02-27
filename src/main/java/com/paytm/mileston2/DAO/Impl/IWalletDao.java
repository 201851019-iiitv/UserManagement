package com.paytm.mileston2.DAO.Impl;

import com.paytm.mileston2.model.Wallet;

public interface IWalletDao {

    void saveWallet(Wallet wallet);                 // save wallet
    Wallet findWalletById(String walletId);       // get wallet by wallet Id.
    void deleteWallet(Wallet w);                //delete wallet by Id.
}
