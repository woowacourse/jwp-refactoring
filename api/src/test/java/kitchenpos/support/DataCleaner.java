package kitchenpos.support;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleaner {

    private static final String IGNORE_FLYWAY = "flyway";

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tables = new ArrayList<>();

    @PostConstruct
    public void findDatabaseTableNames() {
        List<Object[]> tableInfos = entityManager.createNativeQuery("SHOW TABLES").getResultList();
        for (Object[] tableInfo : tableInfos) {
            String tableName = (String) tableInfo[0];
            tables.add(tableName);
        }
    }

    @Transactional
    public void clear() {
        entityManager.clear();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (final String table : tables) {
            truncateExceptFlyway(table);
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void truncateExceptFlyway(final String table) {
        if (table.contains(IGNORE_FLYWAY)) {
            return;
        }
        entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s", table)).executeUpdate();
    }
}
