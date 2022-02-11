package org.paytm.milestone2._Milestone2.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Wallet {
    @Id
    @GeneratedValue
    @Column(name = "Wallet_Id")
    private int walletId;
    @Column(name = "Current_Balance",nullable = false)
    private float currentBalance;
    @Column(name="Mobile_Number",nullable = false,unique = true)
    private String mobileNumber;

}
