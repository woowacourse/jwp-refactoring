package kitchenpos;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void clean() throws SQLException {
        List<String> tableNames = getTableNames();
        tableNames.remove("flyway_schema_history");

        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        }
    }

    private List<String> getTableNames() throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, null, new String[]{"TABLE"});

            List<String> tableNames = new ArrayList<>();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME").toLowerCase(Locale.ROOT);
                tableNames.add(tableName);
            }
            return tableNames;
        }
    }
}
