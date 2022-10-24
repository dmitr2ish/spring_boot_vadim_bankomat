package dmitr2ish.com.github.pseudoBankomat.repository.bank.user;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;

import java.math.BigDecimal;

public interface Accountable {
    User getUserById(String userId);

    BigDecimal getBalance(String userId) throws MoneyException;

    boolean takeMoney(String userId, BigDecimal amount) throws MoneyException;

    boolean putMoney(String userId, BigDecimal amount) throws MoneyException;

    boolean transferMoney(String userIdFrom, String userIdTo, BigDecimal amount, Operation operationOne, Operation operationTwo) throws MoneyException;

    void saveUser(User user);
}
