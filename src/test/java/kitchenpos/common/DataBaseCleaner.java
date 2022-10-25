package kitchenpos.common;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataBaseCleaner implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);

        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE order_table");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("DELETE FROM product WHERE id > 6");
        jdbcTemplate.execute("DELETE FROM menu_group WHERE id > 4");
        jdbcTemplate.execute("DELETE FROM menu WHERE id > 6");
        jdbcTemplate.execute("DELETE FROM menu_product WHERE seq > 6");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");

        jdbcTemplate.execute("ALTER TABLE order_table ALTER COLUMN id RESTART WITH 1");

        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (1, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (2, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (3, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (4, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (5, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (6, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (7, 0, true)");
        jdbcTemplate.execute("INSERT INTO order_table (id, number_of_guests, empty) VALUES (8, 0, true)");
    }
}
