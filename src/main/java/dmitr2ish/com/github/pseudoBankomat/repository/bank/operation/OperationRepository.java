package dmitr2ish.com.github.pseudoBankomat.repository.bank.operation;

import dmitr2ish.com.github.pseudoBankomat.entity.Operation;
import dmitr2ish.com.github.pseudoBankomat.exception.OperationException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

/**
 * @author dmitry ishmitov
 * @date 10/23/22
 * Class for work with Operation entity in data store
 */

@Repository
@Transactional
@ComponentScan("dmitr2ish/com/github/pseudoBankomat/repository")
public class OperationRepository implements Operationable {
    private final Logger log;
    private final EntityManager entityManager;

    @Autowired
    public OperationRepository(EntityManager entityManager, Logger log) {
        this.entityManager = entityManager;
        this.log = log;
    }

    @Override
    public List<Operation> getOperationListByUserId(
            String userId,
            LocalDate beginningDate,
            LocalDate finishingDate) throws OperationException {
        List<Operation> operationList;
        try {
            operationList = createQuery(beginningDate, finishingDate, userId);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new OperationException(ex.getMessage());
        }
        return operationList;
    }

    @Override
    public void saveOperation(Operation operation) {
        entityManager.persist(operation);
    }

    private List<Operation> createQuery(LocalDate beginningDate, LocalDate finishingDate, String userId) {
        var querySelect = "select c from Operation c where ";
        var queryConditionFrom = "c.dateOfOperation >= :from and ";
        var queryConditionTo = "c.dateOfOperation <= :to and ";
        var queryConditionUser = "c.user.id = :id";

        StringBuilder query = new StringBuilder();
        List<Operation> operationList;
        if (beginningDate != null && finishingDate != null) {
            query
                    .append(querySelect)
                    .append(queryConditionFrom)
                    .append(queryConditionTo)
                    .append(queryConditionUser);
            operationList = entityManager.createQuery(query.toString())
                    .setParameter("from", beginningDate)
                    .setParameter("to", finishingDate)
                    .setParameter("id", userId)
                    .getResultList();
        } else if (beginningDate == null && finishingDate != null) {
            query
                    .append(querySelect)
                    .append(queryConditionTo)
                    .append(queryConditionUser);
            operationList = entityManager.createQuery(query.toString())
                    .setParameter("to", finishingDate)
                    .setParameter("id", userId)
                    .getResultList();
        } else if (beginningDate != null) {
            query
                    .append(querySelect)
                    .append(queryConditionFrom)
                    .append(queryConditionUser);
            operationList = entityManager.createQuery(query.toString())
                    .setParameter("from", beginningDate)
                    .setParameter("id", userId)
                    .getResultList();
        } else {
            query
                    .append(querySelect)
                    .append(queryConditionUser);
            operationList = entityManager.createQuery(query.toString())
                    .setParameter("id", userId)
                    .getResultList();
        }
        return operationList;
    }
}
