package kitchenpos.application.support;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class TestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestMethod(final TestContext testContext) {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries();
        truncateTables(jdbcTemplate, truncateQueries);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext()
                .getBean(JdbcTemplate.class);
    }

    private List<String> getTruncateQueries() {
        return List.of(
                "TRUNCATE TABLE orders",
                "TRUNCATE TABLE table_group",
                "TRUNCATE TABLE order_line_item",
                "DELETE FROM product WHERE id > 6",
                "DELETE FROM menu_group WHERE id > 4",
                "DELETE FROM menu WHERE id > 6",
                "DELETE FROM menu_product WHERE seq > 6",
                "DELETE FROM order_table WHERE id > 8"
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
