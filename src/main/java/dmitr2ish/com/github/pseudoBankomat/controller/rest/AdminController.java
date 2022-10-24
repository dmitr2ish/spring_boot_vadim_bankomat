package dmitr2ish.com.github.pseudoBankomat.controller.rest;

import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import dmitr2ish.com.github.pseudoBankomat.service.BankOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author dmitry ishmitov
 * @date 10/23/22
 * TODO description
 */

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final BankOperations service;

    @Autowired
    public AdminController(BankOperations service){
        this.service = service;
    }

    @PostMapping(path = "/saveUser")
    public ResponseEntity<String> saveUser() throws MoneyException {
        User user = new User(UUID.randomUUID().toString(), new BigDecimal(0), null);
        service.saveUser(user);
        return new ResponseEntity<>(
                user.getId(),
                HttpStatus.OK);
    }
}
