package org.paytm.milestone2._Milestone2.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue
    @Column(name = "Txn_Id")
    private int txnId;
    @Column(name = "Payer_Mobile_Number", nullable = false)
    private String payerMobileNumber;
    @Column(name = "Payee_Mobile_Number", nullable = false)
    private String payeeMobileNumber;
    @Column(name = "Amount", nullable = false)
    private float amount;
    @Column(name="Timestamp")
    private Timestamp timestamp;
    @Column(name = "Status")
    private String status;
}
