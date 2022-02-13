package org.paytm.milestone2._Milestone2.controllers;

import org.paytm.milestone2._Milestone2.DTO.Request.TransactionP2pRequestBody;
import org.paytm.milestone2._Milestone2.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<?> transactionP2P(@RequestBody TransactionP2pRequestBody transactionP2pRequestBody){
        return transactionService.transactionP2P(transactionP2pRequestBody);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public ResponseEntity<?> viewTransactionById(@RequestParam("txnId") Integer txnId){
        return transactionService.viewTransactionById(txnId);
    }

    @RequestMapping(value = "/transaction/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> viewTransactionByUserId(@PathVariable Integer userId, @RequestParam("pageNo") Integer pageNo){
        return transactionService.viewTransactionByUserId(userId,pageNo);
    }
}
