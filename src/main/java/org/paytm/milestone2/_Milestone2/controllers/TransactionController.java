package org.paytm.milestone2._Milestone2.controllers;

import org.paytm.milestone2._Milestone2.DTO.Request.TransactionP2pRequestBody;
import org.paytm.milestone2._Milestone2.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transaction", method = RequestMethod.POST)
    public ResponseEntity<?> transactionP2P(@RequestBody TransactionP2pRequestBody transactionP2pRequestBody){
        return transactionService.transactionP2P(transactionP2pRequestBody);
    }
}
