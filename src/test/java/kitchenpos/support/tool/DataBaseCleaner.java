package kitchenpos.support.tool;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataBaseCleaner {

    private static final String COLUMN_LABEL = "TABLE_NAME";
    public static final String TABLE_CLEAR_FORMAT = "TRUNCATE TABLE %s";

    private final JdbcTemplate jdbcTemplate;

    public DataBaseCleaner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void clean() {
        List<String> tableNames = getTableNames();
        tableNames.remove("flyway_schema_history");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            jdbcTemplate.execute(String.format(TABLE_CLEAR_FORMAT, tableName));
        }

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private List<String> getTableNames() {
        final DataSource dataSource = jdbcTemplate.getDataSource();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});

            List<String> tableNames = new ArrayList<>();
            while (rs.next()) {
                String tableName = rs.getString(COLUMN_LABEL).toLowerCase(Locale.ROOT);
                tableNames.add(tableName);
            }
            return tableNames;
        } catch (SQLException e) {
            throw new DatabaseCleanerFailedException(e.getMessage());
        }
    }
}
