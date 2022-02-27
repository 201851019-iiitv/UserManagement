package com.paytm.mileston2.model;


import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.sql.Timestamp;


@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long txnId;

    @Column(name = "payerWalletId", nullable = false)
    public String payerWalletId;

    @Column(name = "payeeWalletId", nullable = false)
    public String payeeWalletId;

    @Column(name = "amount", nullable = false)
    public float amount;

    public Timestamp timestamp;
    //Todo : create Enum for status as "SUCCESS","FAILED","PENDING"
    public String status;


    public Long getTxnId() {
        return txnId;
    }

    public String getPayerWalletId() {
        return payerWalletId;
    }

    public String getPayeeWalletId() {
        return payeeWalletId;
    }

    public float getAmount() {
        return amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public  String getStatus() {
        return status;
    }

    Transaction(TransactionBuilder  transactionBuilder){
        this.amount=transactionBuilder.amount;
        this.status=transactionBuilder.status;
        this.payeeWalletId=transactionBuilder.payeeWalletId;
        this.payerWalletId=transactionBuilder.payerWalletId;
        this.timestamp=transactionBuilder.timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "txnId=" + txnId +
                ", payerWalletId='" + payerWalletId + '\'' +
                ", payeeWalletId='" + payeeWalletId + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }

    //Builder class .
    public static class TransactionBuilder{
        private String payerWalletId;
        private String payeeWalletId;
        private float amount;
        private Timestamp timestamp;
        private String status;


        public TransactionBuilder setPayerWalletId(String payerWalletId) {
            this.payerWalletId = payerWalletId;
            return this;
        }

        public TransactionBuilder setPayeeWalletId(String payeeWalletId) {
            this.payeeWalletId = payeeWalletId;
            return this;
        }

        public TransactionBuilder setAmount(float amount) {
            this.amount = amount;
            return this;
        }

        public TransactionBuilder setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public TransactionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Transaction build(){
            Transaction  transaction = new Transaction(this);
            return transaction;
        }


    }
}
