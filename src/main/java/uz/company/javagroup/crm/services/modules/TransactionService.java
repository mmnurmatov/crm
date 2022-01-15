package uz.isd.javagroup.grandcrm.services.modules;

import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.Transaction;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    List<Transaction> findAll();

    void saveTransaction(Transaction transaction);

    Transaction getTransactionById(Long id) throws RecordNotFoundException;

    Cache proccessKredit(Cache cache, BigDecimal kredit, MoneyType moneyType) throws RecordNotFoundException;

    Cache proccessDebet(Cache cache, BigDecimal debet, Optional<Long> moneyTypeId) throws RecordNotFoundException;

    List<Transaction> findAllByCacheId(Long cacheId);

    List<Transaction> findAllByTransactionStatus(Transaction.TransactionStatus status);
}
