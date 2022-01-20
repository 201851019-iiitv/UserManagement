package Milestone2.Wallet_Management_Project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallet")
public class Wallet {

    @Id
    private String walletId;  // it will be user mobile number.

    @Column(name="CurrBal",nullable = false)
    private float curr_bal;

    // one wallet have many transaction .

}
