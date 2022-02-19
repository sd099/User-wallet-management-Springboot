package org.paytm.milestone2._Milestone2.controllers;

import org.paytm.milestone2._Milestone2.DTO.Request.TransactionP2pRequestBody;
import org.paytm.milestone2._Milestone2.Kafka.Producer;
import org.paytm.milestone2._Milestone2.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Autowired
    Producer producer;

    public String getUserNameFromToken(){
        String UserName = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserName;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<?> transactionP2P(@RequestBody TransactionP2pRequestBody transactionP2pRequestBody){
        String userNameFromToken = getUserNameFromToken();
        ResponseEntity<?> response =  transactionService.transactionP2P(transactionP2pRequestBody,userNameFromToken);
        if(response.getStatusCodeValue()==200){
            producer.publishToTransactionTopic(transactionP2pRequestBody.getAmount()+" Amount transferred from "+transactionP2pRequestBody.getPayerMobileNumber()+" to "+transactionP2pRequestBody.getPayeeMobileNumber());
        }
        return response;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ResponseEntity<?> viewTransactionById(@RequestParam("txnId") Integer txnId){
        return transactionService.viewTransactionById(txnId);
    }

    @RequestMapping(value = "/transaction/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> viewTransactionByUserId(@PathVariable Integer userId, @RequestParam("pageNo") Integer pageNo){
        String userNameFromToken = getUserNameFromToken();
        return transactionService.viewTransactionByUserId(userId,pageNo,userNameFromToken);
    }
}
