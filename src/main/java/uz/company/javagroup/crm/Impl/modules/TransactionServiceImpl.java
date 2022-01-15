package uz.isd.javagroup.grandcrm.Impl.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.isd.javagroup.grandcrm.entity.directories.MoneyType;
import uz.isd.javagroup.grandcrm.entity.modules.Cache;
import uz.isd.javagroup.grandcrm.entity.modules.CacheBalanceData;
import uz.isd.javagroup.grandcrm.entity.modules.Transaction;
import uz.isd.javagroup.grandcrm.exception.RecordNotFoundException;
import uz.isd.javagroup.grandcrm.repository.modules.TransactionRepository;
import uz.isd.javagroup.grandcrm.services.directories.MoneyTypeService;
import uz.isd.javagroup.grandcrm.services.modules.CacheBalanceDataService;
import uz.isd.javagroup.grandcrm.services.modules.CacheService;
import uz.isd.javagroup.grandcrm.services.modules.TransactionService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CacheService cacheService;

    @Autowired
    MoneyTypeService moneyTypeService;

    @Autowired
    CacheBalanceDataService cacheBalanceDataService;


    @Override
    public List<Transaction> findAll() {
        List<Transaction> transactions = (List<Transaction>) transactionRepository.findAll();
        return transactions;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long id) throws RecordNotFoundException {
        Optional<Transaction> transactionOptional = transactionRepository.findById(id);
        if (transactionOptional.isPresent()) return transactionOptional.get();
        else throw new RecordNotFoundException("No Transaction record exists for id: " + id);
    }

    @Override
    @Transactional
    public Cache proccessKredit(Cache cache, BigDecimal kredit, MoneyType moneyType) throws RecordNotFoundException {
        Optional<CacheBalanceData> cacheBalanceData = cacheBalanceDataService.findByCacheId(cache.getId());
        if (cacheBalanceData.isPresent()) {
            if (moneyType.getCode().equals("MoneyInCash")) {
                BigDecimal cacheBalanceDebetForCash = cacheBalanceData.get().getMoneyInCache();
                if (cacheBalanceDebetForCash.compareTo(kredit) >= 0) {
                    BigDecimal balance = cache.getBalance();
                    cache.setBalance(balance.subtract(kredit));
                    cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
                    cacheService.saveCache(cache);
                    cacheBalanceData.get().setMoneyInCache(cacheBalanceDebetForCash.subtract(kredit));
                    cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    return cache;
                }

            } else if (moneyType.getCode().equals("MoneyInCard")) {
                BigDecimal cacheBalanceDebetForCard = cacheBalanceData.get().getMoneyInCard();
                if (cacheBalanceDebetForCard.compareTo(kredit) >= 0) {
                    BigDecimal balance = cache.getBalance();
                    cache.setBalance(balance.subtract(kredit));
                    cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
                    cacheService.saveCache(cache);
                    cacheBalanceData.get().setMoneyInCard(cacheBalanceDebetForCard.subtract(kredit));
                    cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    return cache;
                }

            } else if (moneyType.getCode().equals("MoneyInTerminal")) {
                BigDecimal cacheBalanceDebetForTerminal = cacheBalanceData.get().getMoneyInTerminal();
                if (cacheBalanceDebetForTerminal.compareTo(kredit) >= 0) {
                    BigDecimal balance = cache.getBalance();
                    cache.setBalance(balance.subtract(kredit));
                    cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
                    cacheService.saveCache(cache);
                    cacheBalanceData.get().setMoneyInTerminal(cacheBalanceDebetForTerminal.subtract(kredit));
                    cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    return cache;
                }
            } else if (moneyType.getCode().equals("MoneyInEnumeration")) {
                BigDecimal cacheBalanceDebetForEnumeration = cacheBalanceData.get().getMoneyInEnumeration();
                if (cacheBalanceDebetForEnumeration.compareTo(kredit) >= 0) {
                    BigDecimal balance = cache.getBalance();
                    cache.setBalance(balance.subtract(kredit));
                    cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
                    cacheService.saveCache(cache);
                    cacheBalanceData.get().setMoneyInEnumeration(cacheBalanceDebetForEnumeration.subtract(kredit));
                    cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                    cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    return cache;
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Cache proccessDebet(Cache cache, BigDecimal debet, Optional<Long> moneyTypeId) throws RecordNotFoundException {
        if (debet.compareTo(BigDecimal.ONE) > 0) {
            BigDecimal balance = cache.getBalance();
            cache.setBalance(balance.add(debet));
            cache.setLastTransactionTime(new Timestamp(System.currentTimeMillis()));
            cacheService.saveCache(cache);
            if (moneyTypeId.isPresent()) {
                MoneyType moneyType = moneyTypeService.getMoneyTypeById(moneyTypeId.get());
                Optional<CacheBalanceData> cacheBalanceData = cacheBalanceDataService.findByCacheId(cache.getId());
                if (cacheBalanceData.isPresent()) {
                    if (moneyType.getCode().equals("MoneyInCash")) {
                        BigDecimal cacheBalanceDebetForCash = cacheBalanceData.get().getMoneyInCache();
                        cacheBalanceData.get().setMoneyInCache(cacheBalanceDebetForCash.add(debet));
                        cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    } else if (moneyType.getCode().equals("MoneyInCard")) {
                        BigDecimal cacheBalanceDebetForCard = cacheBalanceData.get().getMoneyInCard();
                        cacheBalanceData.get().setMoneyInCard(cacheBalanceDebetForCard.add(debet));
                        cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    } else if (moneyType.getCode().equals("MoneyInTerminal")) {
                        BigDecimal cacheBalanceDebetForTerminal = cacheBalanceData.get().getMoneyInTerminal();
                        cacheBalanceData.get().setMoneyInTerminal(cacheBalanceDebetForTerminal.add(debet));
                        cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    } else if (moneyType.getCode().equals("MoneyInEnumeration")) {
                        BigDecimal cacheBalanceDebetForEnumeration = cacheBalanceData.get().getMoneyInEnumeration();
                        cacheBalanceData.get().setMoneyInEnumeration(cacheBalanceDebetForEnumeration.add(debet));
                        cacheBalanceData.get().setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceData.get());
                    }
                } else {
                    CacheBalanceData cacheBalanceDataNew = new CacheBalanceData();
                    cacheBalanceDataNew.setCache(cache);
                    if (moneyType.getCode().equals("MoneyInCash")) {
                        cacheBalanceDataNew.setMoneyInCache(debet);
                        cacheBalanceDataNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceDataNew);
                    } else if (moneyType.getCode().equals("MoneyInCard")) {
                        cacheBalanceDataNew.setMoneyInCard(debet);
                        cacheBalanceDataNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceDataNew);
                    } else if (moneyType.getCode().equals("MoneyInTerminal")) {
                        cacheBalanceDataNew.setMoneyInTerminal(debet);
                        cacheBalanceDataNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceDataNew);
                    } else if (moneyType.getCode().equals("MoneyInEnumeration")) {
                        cacheBalanceDataNew.setMoneyInEnumeration(debet);
                        cacheBalanceDataNew.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
                        cacheBalanceDataService.saveCacheBalanceData(cacheBalanceDataNew);
                    }
                }
            }
            return cache;
        }
        return null;
    }

    @Override
    public List<Transaction> findAllByCacheId(Long cacheId) {
        return transactionRepository.findAllByCacheId(cacheId);
    }

    @Override
    public List<Transaction> findAllByTransactionStatus(Transaction.TransactionStatus status) {
        return transactionRepository.findAllByTransactionStatus(status);
    }
}
