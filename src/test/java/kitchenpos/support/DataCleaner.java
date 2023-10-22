package kitchenpos.support;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleaner {

    private static final String CAMEL_CASE_REGEX = "([a-z])([A-Z]+)";
    private static final String SNAKE_CASE_REGEX = "$1_$2";

    private List<String> tableNames;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void findTableNames() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
            .filter(DataCleaner::isEntityClass)
            .map(DataCleaner::changeCamelToSnakeCase)
            .collect(Collectors.toUnmodifiableList());
    }

    private static boolean isEntityClass(final EntityType<?> entity) {
        return entity.getJavaType().getAnnotation(Entity.class) != null;
    }

    private static String changeCamelToSnakeCase(final EntityType<?> entity) {
        final String lowerCase = entity.getName().replaceAll(CAMEL_CASE_REGEX, SNAKE_CASE_REGEX).toLowerCase();
        System.out.println(lowerCase);
        return lowerCase;
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.clear();
        truncate();
    }

    private void truncate() {
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
            entityManager.createNativeQuery(String.format("TRUNCATE TABLE %s;", tableName))
                .executeUpdate();
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }
    }

}
