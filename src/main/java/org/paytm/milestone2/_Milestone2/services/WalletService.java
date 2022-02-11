package org.paytm.milestone2._Milestone2.services;

import org.paytm.milestone2._Milestone2.DTO.Request.AddMoneyRequestBody;
import org.paytm.milestone2._Milestone2.DTO.Request.WalletCreationRequestBody;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.models.User;
import org.paytm.milestone2._Milestone2.models.Wallet;
import org.paytm.milestone2._Milestone2.repository.UserRepository;
import org.paytm.milestone2._Milestone2.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;

    public String getUserNameFromToken(){
        String UserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserName;
    }

    public ResponseEntity<?> createWallet(WalletCreationRequestBody walletCreationRequestBody){

        User user = userRepository.findByMobileNumber(walletCreationRequestBody.getMobileNumber());

        if(user==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Not Found with this Mobile Number"));
        }

        if(user.getUserName().compareTo(getUserNameFromToken())!=0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Unauthorized User"));
        }

        if(walletRepository.findByMobileNumber(walletCreationRequestBody.getMobileNumber())!=null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User already has a wallet"));
        }
        Wallet newWallet = new Wallet();
        newWallet.setCurrentBalance(0.0F);
        newWallet.setMobileNumber(walletCreationRequestBody.getMobileNumber());
        walletRepository.save(newWallet);
        return ResponseEntity.ok(new MessageResponse("Wallet Created Successfully!!"));

    }

    public ResponseEntity<?> addMoneyIntoWallet(AddMoneyRequestBody addMoneyRequestBody){

        User user = userRepository.findByMobileNumber(addMoneyRequestBody.getMobileNumber());

        if(user==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User Not Found with this Mobile Number"));
        }

        if(user.getUserName().compareTo(getUserNameFromToken())!=0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Unauthorized User"));
        }

        Wallet wallet = walletRepository.findByMobileNumber(addMoneyRequestBody.getMobileNumber());

        if(wallet==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Wallet not available. First Create your wallet"));
        }

        if(addMoneyRequestBody.getMoney()<=0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Enter a positive value for money"));
        }

        wallet.setCurrentBalance(wallet.getCurrentBalance()+addMoneyRequestBody.getMoney());
        walletRepository.save(wallet);

        return ResponseEntity.ok(wallet);
    }
}