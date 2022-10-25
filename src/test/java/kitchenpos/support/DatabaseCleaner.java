package kitchenpos.support;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner implements InitializingBean {

    private static final String REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY ";
    private static final String TRUNCATE_QUERY = "TRUNCATE TABLE %s";
    private static final String ID_RESET_QUERY = "ALTER TABLE %s ALTER COLUMN id RESTART WITH 1";
    private static final String SEQ_RESET_QUERY = "ALTER TABLE %s ALTER COLUMN seq RESTART WITH 1";
    private static final List<String> SEQ_TABLE_NAMES = List.of("order_line_item", "menu_product");

    @Autowired
    private DataSource dataSource;

    private final List<String> tableNames = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        try {
            final DatabaseMetaData metaData = dataSource.getConnection()
                    .getMetaData();
            final ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new NoSuchElementException();
        }
        tableNames.remove("flyway_schema_history");
    }

    @Transactional
    public void clear() {
        try (final Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "FALSE")
                    .execute();
            for (String tableName : tableNames) {
                connection.prepareStatement(String.format(TRUNCATE_QUERY, tableName))
                        .execute();
                resetPrimaryKey(connection, tableName);
            }
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "TRUE")
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetPrimaryKey(Connection connection, String tableName) throws SQLException {
        if (SEQ_TABLE_NAMES.contains(tableName.toLowerCase())) {
            connection.prepareStatement(String.format(SEQ_RESET_QUERY, tableName))
                    .execute();
            return;
        }
        connection.prepareStatement(String.format(ID_RESET_QUERY, tableName))
                .execute();
    }
}
