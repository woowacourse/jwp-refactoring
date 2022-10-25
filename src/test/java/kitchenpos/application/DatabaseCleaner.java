package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    private static final String FLYWAY_SCHEMA_NAME = "flyway_schema_history";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void tableClear() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        List<String> tableNames = findTableNames();
        for (String tableName : tableNames) {
            jdbcTemplate.execute(String.format("TRUNCATE TABLE %s RESTART IDENTITY", tableName));
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> findTableNames() {
        List<String> tables = jdbcTemplate.query("SHOW TABLES",
                (resultSet, rowNum) -> resultSet.getString("table_name"));
        return tables.stream().filter(name -> !name.equals(FLYWAY_SCHEMA_NAME)).collect(Collectors.toList());
    }
}
