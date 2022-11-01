package kitchenpos.support.cleaner;

import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataCleaner implements InitializingBean {

    private static final String TRUNCATE_FORMAT = "TRUNCATE TABLE %s";
    private static final String REFERENTIAL_FORMAT = "SET REFERENTIAL_INTEGRITY %s";

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private List<String> tableNames;

    public DataCleaner(final DataSource dataSource, final JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.tableNames = new ArrayList<>();
    }

    @Transactional
    public void clear() {
        executeResetTable();
    }

    private void executeResetTable() {
        jdbcTemplate.execute(String.format(REFERENTIAL_FORMAT, "FALSE"));
        tableNames.stream()
            .filter(tableName -> !tableName.contains("flyway_schema_history"))
            .forEach(tableName -> jdbcTemplate.execute(String.format(TRUNCATE_FORMAT, tableName)));
        jdbcTemplate.execute(String.format(REFERENTIAL_FORMAT, "TRUE"));
    }

    @Override
    public void afterPropertiesSet() {
        try (final var connection = dataSource.getConnection()) {
            final var metaData = connection.getMetaData();
            final var rs = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
        } catch (Exception exception) {
            throw new RuntimeException("Error with Database cleaner");
        }
    }
}
