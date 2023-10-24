package kitchenpos.config;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatabaseCleaner {

    private final List<String> tableNames = new ArrayList<>();

    private final JdbcTemplate jdbcTemplate;

    public DatabaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> findAllTables() {
        return jdbcTemplate.queryForList("show tables").stream()
                .map(it -> it.get("TABLE_NAME"))
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public void truncateTable(String tableName) {
        // 외래 키 체크 비활성화
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");

        // 테이블 truncate
        String truncateQuery = "TRUNCATE TABLE " + tableName;
        jdbcTemplate.execute(truncateQuery);
        System.out.println("Table " + tableName + " truncated successfully.");

        // 외래 키 체크 활성화
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Transactional
    public void clear() {
        for (String tableName : findAllTables()) {
            truncateTable(tableName);
        }
    }
}
