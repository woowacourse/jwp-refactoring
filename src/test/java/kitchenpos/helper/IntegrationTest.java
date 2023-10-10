package kitchenpos.helper;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest extends AbstractTestExecutionListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void init() {
        validateH2Database();
        List<String> truncateQueries = getTruncateQueries();
        truncateAllTables(truncateQueries);
    }

    private void validateH2Database() {
        jdbcTemplate.queryForObject("SELECT H2VERSION() FROM DUAL", String.class);
    }

    private List<String> getTruncateQueries() {
        return jdbcTemplate.queryForList("SELECT CONCAT('TRUNCATE TABLE ', TABLE_NAME, ';') AS q FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String.class);
    }

    private void truncateAllTables(List<String> truncateQueries) {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");

        for (String truncateQuery : truncateQueries) {
            jdbcTemplate.execute(truncateQuery);
        }

        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }
}
