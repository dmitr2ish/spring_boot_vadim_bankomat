package dmitr2ish.com.github.pseudoBankomat.repository.bank.user;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.MoneyException;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;
import dmitr2ish.com.github.pseudoBankomat.repository.bank.operation.OperationRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;

import javax.persistence.EntityManager;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class BankRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private EntityManager entityManager;
    @Mock
    private Logger log;
    @InjectMocks
    private BankRepository repository;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User("test", new BigDecimal(100), null);
    }

    @Test
    public void getUserById() {
        when(entityManager.find(User.class, "test")).thenReturn(user);
        User testUser = repository.getUserById("test");
        assertEquals("User should be equals", user, testUser);
    }

    @Test(expected = NullPointerException.class)
    public void getUserByIdNull() {
        when(entityManager.find(User.class, "test")).thenReturn(null);
        repository.getUserById("test");
    }

    @Test
    public void getBalance() throws MoneyException {
        when(entityManager.find(User.class, "test")).thenReturn(user);
        BigDecimal testBalance = repository.getBalance("test");
        assertEquals("Balance should be equals", new BigDecimal(100), testBalance);
    }

    @Test
    public void takeMoney() throws MoneyException {
        when(entityManager.find(User.class, "test")).thenReturn(user);
        when(entityManager.merge(user)).thenReturn(user);
        boolean testResult = repository.takeMoney("test", new BigDecimal("10"));
        assertTrue("Test result should be true", testResult);
    }

    @Test
    public void putMoney() {
    }

    @Test
    public void transferMoney() {
    }

    @Test
    public void saveUser() {
    }
}