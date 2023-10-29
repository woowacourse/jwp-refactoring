package kitchenpos.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.Type;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner {

    private final List<String> tableNames = new ArrayList<>();

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void getTableNames() throws FileNotFoundException {
        List<String> tableNames = em.getMetamodel().getEntities().stream()
                .map(Type::getJavaType)
                .map(clazz -> clazz.getAnnotation(javax.persistence.Table.class))
                .map(Table::name)
                .collect(Collectors.toList());
        this.tableNames.addAll(tableNames);
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
