package kitchenpos.supports;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        List<String> tableNames = extractTableNames(jdbcTemplate);
        cleanupWithTableNames(jdbcTemplate, tableNames);
    }

    private JdbcTemplate getJdbcTemplate(TestContext testContext) {
        return testContext.getApplicationContext()
                .getBean(JdbcTemplate.class);
    }

    private List<String> extractTableNames(JdbcTemplate jdbcTemplate) {
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'PUBLIC'";
        return jdbcTemplate.queryForList(sql, String.class);
    }

    private void cleanupWithTableNames(JdbcTemplate jdbcTemplate, List<String> tableNames) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        String[] sqlForBatchUpdate = tableNames.stream()
                .map(each -> String.format("TRUNCATE TABLE \"%s\"", each))
                .toArray(String[]::new);
        jdbcTemplate.batchUpdate(sqlForBatchUpdate);

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
