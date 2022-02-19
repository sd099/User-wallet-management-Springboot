package org.paytm.milestone2._Milestone2.services;

import org.paytm.milestone2._Milestone2.DTO.Request.TransactionP2pRequestBody;
import org.paytm.milestone2._Milestone2.DTO.Response.MessageResponse;
import org.paytm.milestone2._Milestone2.Kafka.Producer;
import org.paytm.milestone2._Milestone2.models.Transaction;
import org.paytm.milestone2._Milestone2.models.User;
import org.paytm.milestone2._Milestone2.models.Wallet;
import org.paytm.milestone2._Milestone2.repository.TransactionRepository;
import org.paytm.milestone2._Milestone2.repository.UserRepository;
import org.paytm.milestone2._Milestone2.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public ResponseEntity<?> transactionP2P(TransactionP2pRequestBody transactionP2pRequestBody,String userNameFromToken){
        User payerName = userRepository.findByMobileNumber(transactionP2pRequestBody.getPayerMobileNumber());

        if(payerName==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Payer User Not Found with this Mobile Number"));
        }

        if(payerName.getUserName().compareTo(userNameFromToken)!=0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Unauthorized User"));
        }

        Wallet payerWallet = walletRepository.findByMobileNumber(transactionP2pRequestBody.getPayerMobileNumber());

        if(payerWallet==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Payer Wallet Not Found"));
        }

        if(payerWallet.getCurrentBalance() < transactionP2pRequestBody.getAmount()){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Insufficient balance"));
        }

        Wallet payeeWallet = walletRepository.findByMobileNumber(transactionP2pRequestBody.getPayeeMobileNumber());

        if(payeeWallet==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Payee Wallet Not Found"));
        }

        if(payerWallet==payeeWallet){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Payer wallet and Payee wallet same. Try different wallet"));
        }

        payerWallet.setCurrentBalance(payerWallet.getCurrentBalance()-transactionP2pRequestBody.getAmount());
        payeeWallet.setCurrentBalance(payeeWallet.getCurrentBalance()+transactionP2pRequestBody.getAmount());

        walletRepository.save(payerWallet);
        walletRepository.save(payeeWallet);

        Transaction newTransaction = new Transaction();

        newTransaction.setAmount(transactionP2pRequestBody.getAmount());
        newTransaction.setPayerMobileNumber(transactionP2pRequestBody.getPayerMobileNumber());
        newTransaction.setPayeeMobileNumber(transactionP2pRequestBody.getPayeeMobileNumber());
        newTransaction.setStatus("Success");
        newTransaction.setTimestamp(new Timestamp(System.currentTimeMillis()));

        transactionRepository.save(newTransaction);

        return ResponseEntity.ok(new MessageResponse("Transaction Successfull"));

    }

    public ResponseEntity<?> viewTransactionById(int txnId){
        Transaction transaction = transactionRepository.findById(txnId).orElse(null);

        if(transaction==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Transaction not exist"));
        }

        return ResponseEntity.ok(transaction);
    }

    public ResponseEntity<?> viewTransactionByUserId(int userId,int pageNo,String userNameFromToken){

        User user = userRepository.findById(userId).orElse(null);

        if(user==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No user found"));
        }

        if(user.getUserName().compareTo(userNameFromToken)!=0){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Unauthorized User"));
        }

        Pageable pageable =  PageRequest.of(pageNo,2);

        List<Transaction> transactionsWithPagination = transactionRepository.findByPayerMobileNumberOrPayeeMobileNumber(user.getMobileNumber(),user.getMobileNumber(),pageable);


        if(transactionsWithPagination==null){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No transaction found"));
        }

        return ResponseEntity.ok(transactionsWithPagination);
    }
}
