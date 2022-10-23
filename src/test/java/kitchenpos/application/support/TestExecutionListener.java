package kitchenpos.application.support;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext()
                .getBean(JdbcTemplate.class);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(
                "SELECT Concat('TRUNCATE TABLE ', TABLE_NAME, ';') AS query "
                + "FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                String.class
        );
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE");
        for (final String truncateQuery : truncateQueries) {
            execute(jdbcTemplate, truncateQuery);
        }
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE");
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {
        jdbcTemplate.execute(query);
    }
}
