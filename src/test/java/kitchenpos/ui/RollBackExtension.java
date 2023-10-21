package kitchenpos.ui;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class RollBackExtension implements AfterEachCallback {

    private static final String[] TABLES_TO_PRESERVE = {
            "FLYWAY_SCHEMA_HISTORY"
    };


    @Override
    public void afterEach(final ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        EntityManager entityManager = applicationContext.getBean(EntityManager.class);
        PlatformTransactionManager txManager = applicationContext.getBean(PlatformTransactionManager.class);

        TransactionStatus transactionStatus = txManager.getTransaction(new DefaultTransactionDefinition());

        try {
            entityManager.clear();
            truncate(entityManager);

            txManager.commit(transactionStatus);
        } catch (Exception e) {
            if (!transactionStatus.isCompleted()) {
                txManager.rollback(transactionStatus);
            }
            throw e;
        }
    }

    private void truncate(EntityManager entityManager) {
        final List<String> tableNames = findDatabaseTableNames(entityManager);
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (final String tableName : tableNames) {
            if (!shouldPreserveTable(tableName)) {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            }
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private List<String> findDatabaseTableNames(EntityManager entityManager) {
        final List<String> tableNames = new ArrayList<>();
        final List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        for (final Object[] tableInfo : tableInfos) {
            final String tableName = (String) tableInfo[0];
            tableNames.add(tableName);
        }
        return tableNames;
    }

    private boolean shouldPreserveTable(final String tableName) {
        for (final String tableToPreserve : TABLES_TO_PRESERVE) {
            if (tableToPreserve.equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }
}
