package kitchenpos.common;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseInitializer implements AfterEachCallback {


    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);

        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE menu");
        jdbcTemplate.execute("TRUNCATE TABLE menu_group");
        jdbcTemplate.execute("TRUNCATE TABLE menu_product");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("TRUNCATE TABLE order_line_item");
        jdbcTemplate.execute("TRUNCATE TABLE order_table");
        jdbcTemplate.execute("TRUNCATE TABLE table_group");
        jdbcTemplate.execute("TRUNCATE TABLE product");
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");

        jdbcTemplate.execute("ALTER TABLE menu ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE menu_group ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE menu_product ALTER COLUMN seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE orders ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE order_line_item ALTER COLUMN seq RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE order_table ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE table_group ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE product ALTER COLUMN id RESTART WITH 1");
    }
}
