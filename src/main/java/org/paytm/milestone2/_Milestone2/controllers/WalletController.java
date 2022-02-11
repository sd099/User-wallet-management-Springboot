package org.paytm.milestone2._Milestone2.controllers;

import org.paytm.milestone2._Milestone2.DTO.Request.AddMoneyRequestBody;
import org.paytm.milestone2._Milestone2.DTO.Request.WalletCreationRequestBody;
import org.paytm.milestone2._Milestone2.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @Autowired
    WalletService walletService;

    @RequestMapping(value = "/wallet",method = RequestMethod.POST)
    public ResponseEntity<?> createWallet(@RequestBody WalletCreationRequestBody walletCreationRequestBody){
        return walletService.createWallet(walletCreationRequestBody);
    }

    @RequestMapping(value = "wallet/addmoney",method = RequestMethod.PUT)
    public ResponseEntity<?> addMoneyIntoWallet(@RequestBody AddMoneyRequestBody addMoneyRequestBody){
        return walletService.addMoneyIntoWallet(addMoneyRequestBody);
    }
}
