package kitchenpos.dao;

import javax.sql.DataSource;
import kitchenpos.support.tool.DataBaseCleaner;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public abstract class JdbcTemplateTest {

    @Autowired
    private DataSource dataSource;

    protected JdbcTemplateProductDao jdbcTemplateProductDao;
    protected JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;
    protected JdbcTemplateMenuDao jdbcTemplateMenuDao;
    protected JdbcTemplateMenuProductDao jdbcTemplateMenuProductDao;
    protected JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao;
    protected JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;
    protected JdbcTemplateOrderDao jdbcTemplateOrderDao;
    protected JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @BeforeEach
    void setUp() {
        jdbcTemplateProductDao = new JdbcTemplateProductDao(dataSource);
        jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        jdbcTemplateMenuProductDao = new JdbcTemplateMenuProductDao(dataSource);
        jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);
        DataBaseCleaner dataBaseCleaner = new DataBaseCleaner(new JdbcTemplate(dataSource));
        dataBaseCleaner.clean();
    }
}
