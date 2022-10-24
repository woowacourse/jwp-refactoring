package kitchenpos.application;

import java.util.List;
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
        tableNames = entityManager.createNativeQuery(
                        "SELECT table_name FROM information_schema.tables WHERE table_schema = 'PUBLIC' AND table_name != 'flyway_schema_history'").getResultList();
    }

    @Transactional
    public void clear() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager
                    .createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1")
                    .executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void insertInitialData() {
        // product
        entityManager.createNativeQuery(
                        "INSERT INTO product (name, price) VALUES ('맘모스빵', 3000)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO product (name, price) VALUES ('팥빵', 1000)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO product (name, price) VALUES ('소보로빵', 2000)")
                .executeUpdate();

        // menu_group
        entityManager.createNativeQuery(
                        "INSERT INTO menu_group (name) VALUES ('베이커리')")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO menu_group (name) VALUES ('한마리메뉴')")
                .executeUpdate();

        // table_group
        entityManager.createNativeQuery(
                        "INSERT INTO table_group (id, created_date) VALUES (1, CURRENT_TIMESTAMP)")
                .executeUpdate();

        // order_table
        entityManager.createNativeQuery(
                        "INSERT INTO order_table (id, number_of_guests, empty, table_group_id) VALUES (1, 0, false, 1)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO order_table (id, number_of_guests, empty, table_group_id) VALUES (2, 0, false, 1)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO order_table (id, number_of_guests, empty) VALUES (3, 0, true)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO order_table (id, number_of_guests, empty) VALUES (4, 0, true)")
                .executeUpdate();

        // menu
        entityManager.createNativeQuery(
                        "INSERT INTO menu (id, name, price, menu_group_id) VALUES (1, '후라이드치킨', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO menu (id, name, price, menu_group_id) VALUES (2, '양념치킨', 16000, 2)")
                .executeUpdate();
        entityManager.createNativeQuery(
                        "INSERT INTO menu (id, name, price, menu_group_id) VALUES (3, '반반치킨', 16000, 2);")
                .executeUpdate();
    }
}
