package Milestone2.Wallet_Management_Project.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long txnId;

    @Column(name = "payerWalletId", nullable = false, length = 10)
    public String payerWalletId;

    @Column(name = "payeeWalletId", nullable = false, length = 10)
    public String payeeWalletId;

    @Column(name = "amount", nullable = false)
    public float amount;


    public Timestamp timestamp;
    public String Status;

}
