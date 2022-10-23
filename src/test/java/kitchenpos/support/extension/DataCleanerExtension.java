package kitchenpos.support.extension;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

public class DataCleanerExtension implements BeforeEachCallback {

    @Override
    @Transactional
    public void beforeEach(ExtensionContext context) {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            executeResetTable(jdbcTemplate, rs);
        } catch (Exception exception) {
            throw new RuntimeException("Error with Database cleaner");
        }
    }

    private void executeResetTable(final JdbcTemplate jdbcTemplate, final ResultSet rs) throws SQLException {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if (tableName.equals("flyway_schema_history")) {
                continue;
            }

            jdbcTemplate.execute(createTruncateTable(tableName));
            jdbcTemplate.execute(createResetAutoIncrement(tableName));
        }
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private String createTruncateTable(final String tableName) {
        return "TRUNCATE TABLE " + tableName;
    }

    private String createResetAutoIncrement(final String tableName) {
        return "ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1";
    }
}
