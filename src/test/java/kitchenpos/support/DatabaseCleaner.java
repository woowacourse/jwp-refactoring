package kitchenpos.support;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    private static final String TRUNCATE_SQL_MESSAGE = "TRUNCATE TABLE %s";
    private static final String SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE = "SET REFERENTIAL_INTEGRITY %s";
    private static final String DISABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, false);
    private static final String ENABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, true);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final List<String> tableNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws SQLException {
        DatabaseMetaData databaseMetaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet rs = databaseMetaData.getTables(null, "PUBLIC", null, new String[]{"TABLE"});

        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");
            if (!"flyway_schema_history".equalsIgnoreCase(tableName)) {
                tableNames.add(tableName);
            }
        }
    }

    @Transactional
    public void execute() {
        disableReferentialIntegrity();
        executeTruncate();
        enableReferentialIntegrity();
    }

    private void disableReferentialIntegrity() {
        jdbcTemplate.execute(DISABLE_REFERENTIAL_QUERY);
    }

    private void executeTruncate() {
        tableNames.stream()
                .map(tableName -> String.format(TRUNCATE_SQL_MESSAGE, tableName))
                .forEach(jdbcTemplate::execute);
    }

    private void enableReferentialIntegrity() {
        jdbcTemplate.execute(ENABLE_REFERENTIAL_QUERY);
    }

}
