package kitchenpos.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, String> tableNames = new HashMap<>();

    @PostConstruct
    public void init() {
        final Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();

        for (EntityType<?> entityType : entityTypes) {
            final String tableName = entityType.getJavaType().getAnnotation(Table.class).name();
            final Field[] fields = entityType.getJavaType().getDeclaredFields();
            final String idColumn = extractIdColumn(fields);
            tableNames.put(tableName, idColumn);
        }
    }

    private String extractIdColumn(final Field[] fields) {
        return Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(Id.class))
                .map(Field::getName)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("not exist id"));
    }

    @Transactional
    public void execute() {
        entityManager.flush();

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames.keySet()) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery(
                    "ALTER TABLE " + tableName + " ALTER COLUMN " + tableNames.get(tableName) +
                            " RESTART WITH 1").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

}
