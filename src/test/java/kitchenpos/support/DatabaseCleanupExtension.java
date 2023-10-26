package kitchenpos.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class DatabaseCleanupExtension implements TestExecutionListener {

    private static final Set<String> IGNORE_TABLES = Set.of("flyway_schema_history");
    private static final ThreadLocal<JdbcTemplate> jdbcTemplates = new ThreadLocal<>();
    private static final Set<String> tables = new HashSet<>();

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception {
        ApplicationContext ac = testContext.getApplicationContext();
        JdbcTemplate jdbcTemplate = ac.getBean(JdbcTemplate.class);
        jdbcTemplates.set(jdbcTemplate);
        initialTables(jdbcTemplate);
    }

    private void initialTables(JdbcTemplate jdbcTemplate) {
        if (tables.isEmpty()) {
            tables.addAll(getTables(jdbcTemplate));
        }
    }

    private List<String> getTables(JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1)).stream()
            .filter(table -> !IGNORE_TABLES.contains(table))
            .collect(Collectors.toList());
    }

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        truncate();
    }

    private void truncate() {
        JdbcTemplate jdbcTemplate = jdbcTemplates.get();
        TruncateHelper.h2Truncate(tables, jdbcTemplate);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        jdbcTemplates.remove();
    }
}
