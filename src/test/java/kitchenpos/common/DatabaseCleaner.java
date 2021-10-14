package kitchenpos.common;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Component
public class DatabaseCleaner implements InitializingBean {

    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        try (Connection connection = dataSource.getConnection()) {
            extractTableNames(connection);
        } catch (SQLException ignored) {
        }
    }

    private void extractTableNames(Connection conn) throws SQLException {
        List<String> tableNames = new ArrayList<>();

        ResultSet tables = conn
            .getMetaData()
            .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

        while(tables.next()) {
            tableNames.add(tables.getString("table_name"));
        }

        this.tableNames = tableNames;
    }

    public void execute() {
        try (Connection connection = dataSource.getConnection()) {
            cleanUpDatabase(connection);
        } catch (SQLException ignored) {
        }
    }

    private void cleanUpDatabase(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            if (tableName.toLowerCase().contains("flyway")) {
                continue;
            }
            String alterQuery = getAlterQuery(tableName);
            statement.executeUpdate("TRUNCATE TABLE " + tableName);
            statement.executeUpdate(alterQuery);
        }

        statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
    }

    private String getAlterQuery(String tableName) {
        if (tableName.equalsIgnoreCase("order_line_item")
            || tableName.equalsIgnoreCase("menu_product")) {
            return "ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1";
        }
        return "ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1";
    }
}
