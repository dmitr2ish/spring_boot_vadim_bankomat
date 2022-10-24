package dmitr2ish.com.github.pseudoBankomat.controller.rest;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;
import dmitr2ish.com.github.pseudoBankomat.service.BankOperations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static dmitr2ish.com.github.pseudoBankomat.constant.ProjectConstant.*;

/**
 * @author dmitry ishmitov
 * @date 10/22/22
 * TODO description
 */

@RestController
@RequestMapping("/api/bank")
public class BankController {


    private final BankOperations service;
    private final Logger log;

    @Autowired
    public BankController(BankOperations service,
                          Logger log) {
        this.service = service;
        this.log = log;
    }

    @GetMapping(path = "/getBalance")
    public ResponseEntity<String> getUserBalance(@RequestParam String uuid) throws MoneyException {
        BigDecimal balance;
        balance = service.getBalance(uuid);
        return new ResponseEntity<>(
                String.format("Current balance of user %s is: %s", uuid, balance),
                HttpStatus.OK);
    }

    @PostMapping(path = "/takeMoney")
    public ResponseEntity<String> getUserBalance(
            @RequestParam String uuid,
            @RequestParam BigDecimal amount) throws MoneyException {
        checkingAmountValue(amount);
        var resultOfTakeMoney = String.format("Take amount of money %s from user %s: ", amount, uuid);
        return (service.takeMoney(uuid, amount))
                ? new ResponseEntity<>(resultOfTakeMoney + SUCCESS_OPERATION, HttpStatus.OK)
                : new ResponseEntity<>(resultOfTakeMoney + FAILED_OPERATION, HttpStatus.OK);

    }

    @PostMapping(path = "/putMoney")
    public ResponseEntity<?> putMoney(
            @RequestParam String uuid,
            @RequestParam BigDecimal amount) throws MoneyException {
        checkingAmountValue(amount);
        var resultOfPutMoney = String.format("Put amount of money %s to user %s: ", amount, uuid);
        return (service.putMoney(uuid, amount))
                ? new ResponseEntity<>(resultOfPutMoney + SUCCESS_OPERATION, HttpStatus.OK)
                : new ResponseEntity<>(resultOfPutMoney + FAILED_OPERATION, HttpStatus.OK);
    }

    @GetMapping(path = "/getOperationList")
    public ResponseEntity<List<Operation>> getOperationList(
            @RequestParam String uuid,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) throws OperationException {
        LocalDate dateFrom = checkingDate(from);
        LocalDate dateTo = checkingDate(to);

        List<Operation> operationList = service
                .getOperationListByUserId(
                        uuid,
                        dateFrom,
                        dateTo);
        return new ResponseEntity<>(operationList, HttpStatus.OK);
    }

    @PostMapping(path = "/transferMoney")
    public ResponseEntity<String> transferMoney(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount) throws MoneyException {
        checkingAmountValue(amount);
        var resultOfTransfer = String.format("Transfer amount of money %s from user %s to user %s: ", amount, from, to);
        return (service.transferMoney(from, to, amount))
                ? new ResponseEntity<>(resultOfTransfer + SUCCESS_OPERATION, HttpStatus.OK)
                : new ResponseEntity<>(resultOfTransfer + FAILED_OPERATION, HttpStatus.OK);
    }

    private void checkingAmountValue(BigDecimal amount) throws MoneyException {
        String errorMessage;
        if (amount.signum() <= 0) {
            errorMessage = String.format("Amount value: %s is incorrect, should be more than 0", amount);
            log.info(errorMessage);
            throw new MoneyException(errorMessage);
        }
    }

    private LocalDate checkingDate(String date) throws OperationException {
        if (date == null) {
            return null;
        }
        String errorMessage;
        LocalDate resultDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        try {
            resultDate = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            errorMessage = String.format("Some problem with date: %s, please use this date format %s",
                    date, DATE_FORMAT);
            throw new OperationException(errorMessage);
        }
        return resultDate;
    }

    @ExceptionHandler(MoneyException.class)
    public ResponseEntity<String> moneyExceptionHandler(MoneyException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }

    @ExceptionHandler(OperationException.class)
    public ResponseEntity<String> operationExceptionHandler(OperationException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.OK);
    }
}
