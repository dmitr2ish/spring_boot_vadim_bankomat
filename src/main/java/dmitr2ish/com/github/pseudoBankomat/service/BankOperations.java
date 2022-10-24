package dmitr2ish.com.github.pseudoBankomat.service;


import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author dmitry ishmitov
 * @date 10/22/22
 * TODO description
 */

public interface BankOperations {
    List<Operation> getOperationListByUserId(String userId, LocalDate beginningDate,
                                             LocalDate finishingDate) throws OperationException;
    BigDecimal getBalance(String userId) throws MoneyException;
    boolean takeMoney(String userId, BigDecimal amount) throws MoneyException;
    boolean putMoney(String userId, BigDecimal amount) throws MoneyException;
    boolean transferMoney(String userIdFrom,String userIdTo, BigDecimal amount) throws MoneyException;
    void saveUser(User user);
}
