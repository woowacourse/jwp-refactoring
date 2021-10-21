package kitchenpos.api;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ApiTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() throws SQLException {
        truncateAllTables();
    }

    private void truncateAllTables() throws SQLException {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, getAllTables().toArray(new String[0]));
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private List<String> getAllTables() throws SQLException {
        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        List<String> tables = new ArrayList<>();
        try (ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"})) {
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        }
        tables.remove("flyway_schema_history");
        return tables;
    }
}
