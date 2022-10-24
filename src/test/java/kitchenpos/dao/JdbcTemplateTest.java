package kitchenpos.dao;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;

@JdbcTest
public class JdbcTemplateTest {

    @Autowired
    private DataSource dataSource;

    protected JdbcTemplateProductDao jdbcTemplateProductDao;
    protected JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
    }
}
