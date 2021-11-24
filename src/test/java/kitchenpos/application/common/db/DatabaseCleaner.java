package kitchenpos.application.common.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner {

    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    @PostConstruct
    public void postConstruct() {
        try (Connection connection = dataSource.getConnection()) {
            tableNames = extractTableNames(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<String> extractTableNames(Connection conn) throws SQLException {
        List<String> tableNames = new ArrayList<>();

        try (ResultSet tables = conn
            .getMetaData()
            .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})
        ) {
            while (tables.next()) {
                tableNames.add(tables.getString("table_name"));
            }

            return tableNames;
        }
    }

    public void clear() {
        try (Connection connection = dataSource.getConnection()) {
            cleanUpDatabase(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void cleanUpDatabase(Connection conn) throws SQLException {
        try (Statement statement = conn.createStatement()) {

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

            for (String tableName : tableNames) {
                try {
                    statement.executeUpdate("TRUNCATE TABLE " + tableName);
                } catch (SQLException ignore) {
                }
            }

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }
}
