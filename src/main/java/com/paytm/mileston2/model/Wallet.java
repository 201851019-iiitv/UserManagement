package com.paytm.mileston2.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


// one wallet have many transaction .
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    private String walletId;  // it will be user mobile number.

    @Column(name="Curr_bal",nullable = false)
    private float currBal;

    @Override
    public String toString() {
        return "Wallet{" +
                "walletId='" + walletId + '\'' +
                ", currBal=" + currBal +
                '}';
    }

}
