package kitchenpos.support;

import java.util.HashSet;
import java.util.Set;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class DatabaseCleanupExtension implements TestExecutionListener {

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
            tables.addAll(jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1)));
        }
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
