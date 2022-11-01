package kitchenpos.support;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataCleaner {

    private final JdbcTemplate jdbcTemplate;

    public DataCleaner(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void clean() {
        final String sql = "SET foreign_key_checks = 0;\n"
                + "\n"
                + "TRUNCATE TABLE menu;\n"
                + "TRUNCATE TABLE menu_group;\n"
                + "TRUNCATE TABLE menu_product;\n"
                + "TRUNCATE TABLE product;\n"
                + "TRUNCATE TABLE order_line_item;\n"
                + "TRUNCATE TABLE orders;\n"
                + "TRUNCATE TABLE order_table;\n"
                + "TRUNCATE TABLE table_group;\n"
                + "\n"
                + "SET foreign_key_checks = 1;";
        jdbcTemplate.update(sql);
    }
}
