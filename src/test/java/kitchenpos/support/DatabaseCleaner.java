package kitchenpos.support;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    private static final String TRUNCATE_TABLE_QUERY = "SELECT Concat('TRUNCATE TABLE \"', TABLE_NAME, '\";')\n" +
        "FROM INFORMATION_SCHEMA.TABLES\n" +
        "WHERE TABLE_SCHEMA = 'PUBLIC'\n";

    @Override
    public void beforeTestMethod(final TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        List<String> truncateTableQueries = getTruncateTableQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateTableQueries);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext()
            .getBean(JdbcTemplate.class);
    }

    private List<String> getTruncateTableQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(TRUNCATE_TABLE_QUERY, String.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateTableQueries) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        truncateTableQueries.forEach(jdbcTemplate::execute);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}

