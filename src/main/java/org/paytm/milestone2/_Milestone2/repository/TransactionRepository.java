package org.paytm.milestone2._Milestone2.repository;

import org.paytm.milestone2._Milestone2.models.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction,Integer> {
}
