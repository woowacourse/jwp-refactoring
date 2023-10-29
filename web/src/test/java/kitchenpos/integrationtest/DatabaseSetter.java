package kitchenpos.integrationtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSetter {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void setUp() {
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0;");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE;");
        jdbcTemplate.execute("TRUNCATE TABLE menu;");
        jdbcTemplate.execute("TRUNCATE TABLE menu_group;");
        jdbcTemplate.execute("TRUNCATE TABLE menu_product;");
        jdbcTemplate.execute("TRUNCATE TABLE order_line_item;");
        jdbcTemplate.execute("TRUNCATE TABLE orders;");
        jdbcTemplate.execute("TRUNCATE TABLE order_table;");
        jdbcTemplate.execute("TRUNCATE TABLE product;");
        jdbcTemplate.execute("TRUNCATE TABLE table_group;");
//        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1;");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE;");
    }
}
