package org.paytm.milestone2._Milestone2.controllers;

import org.paytm.milestone2._Milestone2.DTO.Request.AddMoneyRequestBody;
import org.paytm.milestone2._Milestone2.DTO.Request.WalletCreationRequestBody;
import org.paytm.milestone2._Milestone2.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {

    @Autowired
    WalletService walletService;

    public String getUserNameFromToken(){
        String UserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserName;
    }

    @RequestMapping(value = "/wallet",method = RequestMethod.POST)
    public ResponseEntity<?> createWallet(@RequestBody WalletCreationRequestBody walletCreationRequestBody){
        String userNameFromToken = getUserNameFromToken();
        return walletService.createWallet(walletCreationRequestBody,userNameFromToken);
    }

    @RequestMapping(value = "wallet/addmoney",method = RequestMethod.PUT)
    public ResponseEntity<?> addMoneyIntoWallet(@RequestBody AddMoneyRequestBody addMoneyRequestBody){
        String userNameFromToken = getUserNameFromToken();
        return walletService.addMoneyIntoWallet(addMoneyRequestBody,userNameFromToken);
    }
}
