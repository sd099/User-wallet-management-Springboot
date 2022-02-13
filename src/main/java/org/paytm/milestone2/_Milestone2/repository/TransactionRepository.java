package org.paytm.milestone2._Milestone2.repository;

import org.paytm.milestone2._Milestone2.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    Page<Transaction> findByPayerMobileNumberOrPayeeMobileNumber(String payerNum, String payeeNum, Pageable pageable);
}
