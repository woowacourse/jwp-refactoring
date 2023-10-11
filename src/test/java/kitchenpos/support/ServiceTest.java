package kitchenpos.support;


import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
public class ServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        truncate();
    }

    private void truncate() {
        jdbcTemplate.execute(String.format("SET FOREIGN_KEY_CHECKS %d", 0));
        List<String> tables = jdbcTemplate.query("SHOW TABLES", (rs, rowNum) -> rs.getString(1));
        for (String tableName : tables) {
            jdbcTemplate.execute(String.format("TRUNCATE TABLE %s", tableName));
        }
        jdbcTemplate.execute(String.format("SET FOREIGN_KEY_CHECKS %d", 1));
    }
}
