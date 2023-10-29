package kitchenpos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    private final List<String> tableNames = new ArrayList<>();

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllTables() {
        return jdbcTemplate.queryForList("SHOW TABLES").stream()
                .map(it -> it.get("TABLE_NAME").toString())
                .filter(it -> !it.equals("flyway_schema_history"))
                .collect(Collectors.toList());
    }

    public void truncateTable(final String tableName) {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Transactional
    public void clear() {
        for (String tableName : findAllTables()) {
            truncateTable(tableName);
        }
    }
}
