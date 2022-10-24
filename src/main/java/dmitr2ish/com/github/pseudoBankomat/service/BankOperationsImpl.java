package dmitr2ish.com.github.pseudoBankomat.service;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.TypeOfOperation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;
import dmitr2ish.com.github.pseudoBankomat.repository.bank.operation.Operationable;
import dmitr2ish.com.github.pseudoBankomat.repository.bank.user.Accountable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @author dmitry ishmitov
 * @date 10/22/22
 * TODO description
 */

@Service
@ComponentScan("dmitr2ish/com/github/pseudoBankomat")
public class BankOperationsImpl implements BankOperations {

    private final Accountable bankRepo;
    private final Operationable operationRepo;

    @Autowired
    public BankOperationsImpl(Accountable bankRepo, Operationable operationRepo) {
        this.bankRepo = bankRepo;
        this.operationRepo = operationRepo;
    }

    @Override
    public List<Operation> getOperationListByUserId(
            String userId,
            LocalDate beginningDate,
            LocalDate finishingDate) throws OperationException {
        return operationRepo.getOperationListByUserId(userId, beginningDate, finishingDate);
    }

    @Override
    public BigDecimal getBalance(String userId) throws MoneyException {
        return bankRepo.getBalance(userId);
    }

    @Override
    public boolean takeMoney(String userId, BigDecimal amount) throws MoneyException {
        createOperation(userId, amount, TypeOfOperation.WITHDRAW);
        return bankRepo.takeMoney(userId, amount);
    }

    @Override
    public boolean putMoney(String userId, BigDecimal amount) throws MoneyException {
        var operation = createOperation(userId, amount, TypeOfOperation.DEPOSIT);
        operationRepo.saveOperation(operation);
        return bankRepo.putMoney(userId, amount);
    }

    @Override
    public boolean transferMoney(String userIdFrom, String userIdTo, BigDecimal amount) throws MoneyException {
        var operationOne = createOperation(userIdFrom, amount, TypeOfOperation.SENT);
        var operationTwo = createOperation(userIdTo, amount, TypeOfOperation.RECEIVED);
        return bankRepo.transferMoney(userIdFrom, userIdTo, amount, operationOne, operationTwo);
    }

    @Override
    public void saveUser(User user) {
        bankRepo.saveUser(user);
    }

    private Operation createOperation(String userId, BigDecimal amount, TypeOfOperation typeOfOperation) {
        var user = bankRepo.getUserById(userId);
        var operation = new Operation(UUID.randomUUID().toString(), LocalDate.now(), typeOfOperation, amount, user);
        operationRepo.saveOperation(operation);
        return operation;
    }
}
