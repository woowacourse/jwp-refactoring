package kitchenpos.support;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleanUp {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clear() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        List<String> tableNames = findTableNames();
        for (String tableName : tableNames) {
            jdbcTemplate.execute(String.format("TRUNCATE TABLE %s RESTART IDENTITY", tableName));
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> findTableNames() {
        return jdbcTemplate.query("SHOW TABLES",
                (resultSet, rowNum) -> resultSet.getString("table_name"));
    }

}
