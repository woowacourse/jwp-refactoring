package kitchenpos;

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
public class DatabaseCleanup implements InitializingBean {

    private static final String TRUNCATE_SQL_MESSAGE = "TRUNCATE TABLE %s";
    private static final String SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE = "SET REFERENTIAL_INTEGRITY %s";
    private static final String DISABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, false);
    private static final String ENABLE_REFERENTIAL_QUERY = String.format(SET_REFERENTIAL_INTEGRITY_SQL_MESSAGE, true);
    private final List<String> tableNames = new ArrayList<>();
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterPropertiesSet() throws SQLException {
        ResultSet rs = jdbcTemplate.getDataSource()
                .getConnection()
                .getMetaData()
                .getTables(null, "PUBLIC", null, new String[]{"TABLE"});

        while (rs.next()) {
            String tableName = rs.getString("TABLE_NAME");

            this.tableNames.add(tableName);
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
                .filter(name -> !name.contains("flyway"))
                .forEach(this::executeTruncateQuery);
    }

    private void executeTruncateQuery(String tableName) {
        String TRUNCATE_QUERY = String.format(TRUNCATE_SQL_MESSAGE, tableName);

        jdbcTemplate.execute(TRUNCATE_QUERY);
    }

    private void enableReferentialIntegrity() {
        jdbcTemplate.execute(ENABLE_REFERENTIAL_QUERY);
    }

}
