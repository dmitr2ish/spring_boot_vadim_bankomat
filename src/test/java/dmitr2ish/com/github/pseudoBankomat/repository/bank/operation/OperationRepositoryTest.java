package dmitr2ish.com.github.pseudoBankomat.repository.bank.operation;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.entity.TypeOfOperation;
import dmitr2ish.com.github.pseudoBankomat.entity.User;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OperationRepositoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Mock
    private EntityManager entityManager;
    @Mock
    private Logger log;
    @InjectMocks
    private OperationRepository repository;
    private Operation operation;


    @Before
    public void setUp() {
        operation = new Operation("test", LocalDate.now(), TypeOfOperation.WITHDRAW, new BigDecimal(0), new User());
    }

    @Test(expected = OperationException.class)
    public void getOperationListByUserId() throws OperationException {
        List<Operation> operationListTest = new ArrayList<>();
        operationListTest.add(operation);

        var querySelect = "select c from Operation c where ";
        var queryConditionFrom = "c.dateOfOperation >= :from and ";
        var queryConditionTo = "c.dateOfOperation <= :to and ";
        var queryConditionUser = "c.user.id = :id";

        LocalDate beginningDate = LocalDate.now();
        LocalDate finishingDate = LocalDate.now();
        String userId = "test";

        String query1 = querySelect +
                queryConditionFrom +
                queryConditionTo +
                queryConditionUser;

        String query2 = querySelect +
                queryConditionTo +
                queryConditionUser;

        String query3 = querySelect +
                queryConditionFrom +
                queryConditionUser;

        String query4 = querySelect +
                queryConditionUser;

        Query testQuery = mock(Query.class);

        when(entityManager.createQuery(query1)).thenReturn(testQuery);
        when(entityManager.createQuery(query2)).thenReturn(testQuery);
        when(entityManager.createQuery(query3)).thenReturn(testQuery);
        when(entityManager.createQuery(query4)).thenReturn(testQuery);

        when(testQuery.setParameter("from", beginningDate)).thenReturn(testQuery);
        when(testQuery.setParameter("to", finishingDate)).thenReturn(testQuery);
        when(testQuery.setParameter("id", userId)).thenReturn(testQuery);
        when(testQuery.getResultList()).thenReturn(operationListTest);


        List<Operation> operationList1 = repository.getOperationListByUserId(userId, beginningDate, finishingDate);
        assertEquals("lists must be equals", operationListTest, operationList1);
        List<Operation> operationList2 = repository.getOperationListByUserId(userId, null, finishingDate);
        assertEquals("lists must be equals", operationListTest, operationList2);
        List<Operation> operationList3 = repository.getOperationListByUserId(userId, beginningDate, null);
        assertEquals("lists must be equals", operationListTest, operationList3);
        List<Operation> operationList4 = repository.getOperationListByUserId(userId, null, null);
        assertEquals("lists must be equals", operationListTest, operationList4);

        doThrow(OperationException.class).when(repository.getOperationListByUserId(null, null, null));
    }

    @Test
    public void saveOperation() {
        repository.saveOperation(operation);
        verify(entityManager).persist(operation);
        verify(entityManager, times(1)).persist(operation);
    }
}