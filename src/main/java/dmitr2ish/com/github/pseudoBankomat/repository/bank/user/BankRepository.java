package dmitr2ish.com.github.pseudoBankomat.repository.bank.user;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * @author dmitry ishmitov
 * @date 10/22/22
 * TODO description
 */

@Repository
@Transactional
@ComponentScan("dmitr2ish/com/github/pseudoBankomat/repository")
public class BankRepository implements Accountable {
    private final Logger log;
    private final EntityManager entityManager;

    @Autowired
    public BankRepository(EntityManager entityManager, Logger log) {
        this.entityManager = entityManager;
        this.log = log;
    }

    @Override
    public User getUserById(String userId) {
        User user = entityManager.find(User.class, userId);
        checkingUserIsNull(user);
        return user;
    }

    @Override
    public BigDecimal getBalance(String userId) throws MoneyException {
        User user;
        try {
            user = entityManager.find(User.class, userId);
            checkingUserIsNull(user);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new MoneyException(ex.getMessage());
        }
        return user.getBalance();
    }

    @Override
    public boolean takeMoney(String userId, BigDecimal amount) throws MoneyException {
        User user;
        try {
            user = entityManager.find(User.class, userId);
            checkingUserIsNull(user);
            BigDecimal initialAmount = user.getBalance();
            if (initialAmount.compareTo(amount) < 0) {
                throw new MoneyException(
                        String.format("Don't have money, " +
                                "current balance %s, need to take %s", user.getBalance(), amount));
            } else {
                user.setBalance(initialAmount.subtract(amount));
                user = entityManager.merge(user);
                checkingUserIsNull(user);
                return true;
            }
        } catch (Exception ex) {
            log.error(String.valueOf(ex));
            throw new MoneyException(ex.getMessage());
        }
    }

    @Override
    public boolean putMoney(String userId, BigDecimal amount) throws MoneyException {
        User user;
        try {
            user = entityManager.find(User.class, userId);
            checkingUserIsNull(user);
            BigDecimal initialAmount = user.getBalance();

            user.setBalance(initialAmount.add(amount));
            user = entityManager.merge(user);
            checkingUserIsNull(user);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new MoneyException(ex.getMessage());
        }
    }

    @Override
    public boolean transferMoney(String userIdFrom, String userIdTo, BigDecimal amount,
                                 Operation operationOne, Operation operationTwo) throws MoneyException {
        User userFrom;
        User userTo;
        try {
            userFrom = getUserByUserId(userIdFrom);
            BigDecimal initialAmountUserFrom = userFrom.getBalance();
            if (initialAmountUserFrom.compareTo(amount) < 0) {
                throw new MoneyException(
                        String.format("Don't have money, " +
                                        "current balance %s of user %s, need to take %s",
                                initialAmountUserFrom, userIdFrom, amount));
            } else {
                BigDecimal resultAmountUserFrom = initialAmountUserFrom.subtract(amount);
                checkingUserIsNull(userFrom);
                userFrom.setBalance(resultAmountUserFrom);

                userTo = getUserByUserId(userIdFrom);
                BigDecimal initialAmountUserTo = userTo.getBalance();

                BigDecimal resultAmountUserTo = initialAmountUserTo.add(amount);
                userTo.setBalance(resultAmountUserTo);

                userFrom = entityManager.merge(userFrom);
                userTo = entityManager.merge(userTo);

                checkingUserIsNull(userFrom);
                checkingUserIsNull(userTo);
                if (userFrom.getBalance().equals(resultAmountUserFrom) ||
                        userTo.getBalance().equals(resultAmountUserTo)) {
                    return true;
                } else {
                    userFrom.setBalance(initialAmountUserFrom);
                    userTo.setBalance(initialAmountUserTo);
                    entityManager.merge(userFrom);
                    entityManager.merge(userTo);
                    entityManager.remove(operationOne);
                    entityManager.remove(operationTwo);
                    throw new MoneyException("Money don't except right value, rollback to initial values");
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new MoneyException(ex.getMessage());
        }
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }


    private User getUserByUserId(String userId) {
        User user = entityManager.find(User.class, userId);
        checkingUserIsNull(user);
        return user;
    }

    private static void checkingUserIsNull(User user) {
        if (user == null) {
            throw new NullPointerException("This user is absent");
        }
    }
}
