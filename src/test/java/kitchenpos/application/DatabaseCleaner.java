package kitchenpos.application;

import com.google.common.base.CaseFormat;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel()
                .getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager
                    .createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_id RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void insertInitialData() {
        entityManager.createNativeQuery("INSERT INTO menu_group (menu_group_id, name) VALUES (1, '두마리메뉴')")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_group (menu_group_id, name) VALUES (2, '한마리메뉴')")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_group (menu_group_id, name) VALUES (3, '순살파닭두마리메뉴')")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_group (menu_group_id, name) VALUES (4, '신메뉴')")
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (1, '후라이드', 16000)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (2, '양념치킨', 16000)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (3, '반반치킨', 16000)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (4, '통구이', 16000)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (5, '간장치킨', 17000)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO product (product_id, name, price) VALUES (6, '순살치킨', 17000)")
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (3, '반반치킨', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (4, '통구이', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (5, '간장치킨', 17000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu (menu_id, name, price, menu_group_id) VALUES (6, '순살치킨', 17000, 2)")
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (1, 1, 1)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (2, 2, 1)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (3, 3, 1)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (4, 4, 1)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (5, 5, 1)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO menu_product (menu_id, product_id, quantity) VALUES (6, 6, 1)")
                .executeUpdate();

        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (1, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (2, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (3, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (4, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (5, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (6, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (7, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery("INSERT INTO order_table (order_table_id, number_of_guests, empty) VALUES (8, 0, true)")
                .executeUpdate();
    }
}
