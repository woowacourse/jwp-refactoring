package kitchenpos.common;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ServiceTestExecutionListener extends AbstractTestExecutionListener {

    private static final List<String> INCLUDE_ID_TABLE_NAMES
            = List.of("MENU", "MENU_GROUP", "ORDERS", "ORDER_TABLE", "PRODUCT", "TABLE_GROUP");
    private static final List<String> INCLUDE_SEQ_TABLE_NAMES
            = List.of("MENU_PRODUCT", "ORDER_LINE_ITEM");

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        final List<String> truncateQueries = getTruncateQueries(jdbcTemplate);
        truncateTables(jdbcTemplate, truncateQueries);
        resetAutoIncrement(jdbcTemplate);
    }

    private List<String> getTruncateQueries(final JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForList(
                "SELECT Concat('DELETE FROM ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
                String.class);
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    private void truncateTables(final JdbcTemplate jdbcTemplate, final List<String> truncateQueries) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE;");
        for (final String truncateQuery : truncateQueries) {
            if (!truncateQuery.equalsIgnoreCase("DELETE FROM flyway_schema_history;")) {
                execute(jdbcTemplate, truncateQuery);
            }
        }
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE;");
    }

    private void resetAutoIncrement(final JdbcTemplate jdbcTemplate) {
        final List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
        for (final String tableName : tableNames) {
            if (INCLUDE_ID_TABLE_NAMES.contains(tableName)) {
                final String resetQuery = String.format("ALTER TABLE %s ALTER COLUMN ID RESTART WITH 1;", tableName);
                execute(jdbcTemplate, resetQuery);
            }
            if (INCLUDE_SEQ_TABLE_NAMES.contains(tableName)) {
                final String resetQuery = String.format("ALTER TABLE %s ALTER COLUMN SEQ RESTART WITH 1;", tableName);
                execute(jdbcTemplate, resetQuery);
            }
        }
    }

    private void execute(final JdbcTemplate jdbcTemplate, final String query) {
        jdbcTemplate.execute(query);
    }
}
