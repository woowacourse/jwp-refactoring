package kitchenpos.repository.support;

import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DataCleaner extends AbstractTestExecutionListener {

    private static List<String> tables = new ArrayList<>();

    @Override
    public void prepareTestInstance(TestContext testContext) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        if (tables.isEmpty()) {
            List<String> allTables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
            allTables.remove(allTables.indexOf("flyway_schema_history"));
            tables.addAll(allTables);
        }
    }

    private JdbcTemplate getJdbcTemplate(final TestContext testContext) {
        return testContext.getApplicationContext().getBean(JdbcTemplate.class);
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        final JdbcTemplate jdbcTemplate = getJdbcTemplate(testContext);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : tables) {
            jdbcTemplate.execute("TRUNCATE TABLE " + table + " RESTART IDENTITY");
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
