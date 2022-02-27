package com.paytm.mileston2.Repository;

import com.paytm.mileston2.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;



public interface WalletRepo extends JpaRepository<Wallet ,String> {
}
