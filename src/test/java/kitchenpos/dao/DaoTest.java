package kitchenpos.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@TestInstance(Lifecycle.PER_CLASS)
public class DaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeAll
    public void setUp() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE orders");
        jdbcTemplate.execute("TRUNCATE TABLE product");
        jdbcTemplate.execute("TRUNCATE TABLE order_line_item");
        jdbcTemplate.execute("TRUNCATE TABLE menu");
        jdbcTemplate.execute("TRUNCATE TABLE menu_group");
        jdbcTemplate.execute("TRUNCATE TABLE menu_product");
        jdbcTemplate.execute("TRUNCATE TABLE order_table");
        jdbcTemplate.execute("TRUNCATE TABLE table_group");
    }
}
