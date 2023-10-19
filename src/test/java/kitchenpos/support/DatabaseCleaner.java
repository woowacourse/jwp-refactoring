package kitchenpos.support;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class DatabaseCleaner extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        JdbcTemplate jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        truncateAllData(jdbcTemplate);
    }

    private void truncateAllData(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(String.format("SET FOREIGN_KEY_CHECKS %d", 0));
        List<String> tables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
        for (String tableName : tables) {
            jdbcTemplate.execute(String.format("TRUNCATE TABLE %s", tableName));
        }
        jdbcTemplate.execute(String.format("SET FOREIGN_KEY_CHECKS %d", 1));
    }
}
