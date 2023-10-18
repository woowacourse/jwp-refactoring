package kitchenpos.support;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner {

    private final List<String> tableNames = new ArrayList<>();
    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void getTableNames() {
        List<String> tableNames = em.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .map(Class::getSimpleName)
                .map(this::getTableName)
                .collect(Collectors.toList());
        this.tableNames.addAll(tableNames);
    }

    private String getTableName(String className) {
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(className.charAt(0)));
        for (int i = 1; i < className.length(); i++) {
            if (Character.isUpperCase(className.charAt(i))) {
                sb.append("_").append(Character.toLowerCase(className.charAt(i)));
                continue;
            }
            sb.append(className.charAt(i));
        }
        return sb.toString();
    }

    @Transactional
    public void clean() {
        em.flush();
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            em.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
