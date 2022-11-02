package kitchenpos.support;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseManager {

    private static final Logger log = LoggerFactory.getLogger(DatabaseManager.class);

    private final EntityManager entityManager;
    private final List<String> tableNames;

    public DatabaseManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.tableNames = extractTableNames(entityManager);
    }

    private List<String> extractTableNames(EntityManager entityManager) {
        return entityManager.getMetamodel().getEntities().stream()
                .filter(this::isEntity)
                .map(this::convertCamelToSnake)
                .collect(Collectors.toList());
    }

    private boolean isEntity(EntityType<?> entityType) {
        return entityType.getJavaType().getAnnotation(Entity.class) != null;
    }

    private String convertCamelToSnake(EntityType<?> entityType) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return entityType.getName()
                .replaceAll(regex, replacement)
                .toUpperCase();
    }

    @Transactional
    public void truncateTables() {
        log.info("------------------------------------ 테스트 종료 ------------------------------------");
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            if (tableName.equals("ORDER")) {
                tableName = "ORDERS";
            }
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            if (tableName.equals("MENU_PRODUCT") || tableName.equals("ORDER_LINE_ITEM")) {
                entityManager.createNativeQuery(
                        "ALTER TABLE " + tableName + " ALTER COLUMN " + "seq" + " RESTART WITH 1"
                ).executeUpdate();
                continue;
            }
            entityManager.createNativeQuery(
                    "ALTER TABLE " + tableName + " ALTER COLUMN " + "id" + " RESTART WITH 1"
            ).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void setUp() {
        log.info("------------------------------------ 테스트 시작 ------------------------------------");
    }
}
