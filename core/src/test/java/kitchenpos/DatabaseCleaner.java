package kitchenpos;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String[] TABLES_TO_PRESERVE = {
            "FLYWAY_SCHEMA_HISTORY"
    };

    public void clear() {
        entityManager.clear();
        truncate();
    }

    private List<String> findDatabaseTableNames() {
        final List<String> tableNames = new ArrayList<>();
        final List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        for (final Object[] tableInfo : tableInfos) {
            final String tableName = (String) tableInfo[0];
            tableNames.add(tableName);
        }
        return tableNames;
    }

    private void truncate() {
        final List<String> tableNames = findDatabaseTableNames();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (final String tableName : tableNames) {
            if (!shouldPreserveTable(tableName)) {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            }
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
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
