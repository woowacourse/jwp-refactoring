package kitchenpos;

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
public class DatabaseCleanner implements InitializingBean {
    @Autowired
    private DataSource dataSource;

    private final List<String> tableNames = new ArrayList<>();

    private final List<String> seqTables = List.of("order_line_item", "menu_product");

    @Override
    public void afterPropertiesSet() throws Exception {
        try (final var connection = dataSource.getConnection()) {
            final var tableResultSet = connection.getMetaData()
                    .getTables(null, null, null, new String[]{"TABLE"});

            while (tableResultSet.next()) {
                this.tableNames.add(tableResultSet.getString("TABLE_NAME").toLowerCase());
            }

            tableNames.remove("flyway_schema_history");
        } catch (SQLException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }

    @Transactional
    public void clear() {
        try (final var connection = dataSource.getConnection();
             final var statement = connection.createStatement()) {

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

            for (String tableName : tableNames) {
                statement.executeUpdate("TRUNCATE TABLE " + tableName);
                statement.executeUpdate(
                        "ALTER TABLE " + tableName + " ALTER COLUMN " + getPkName(tableName) + " RESTART WITH 1");
            }

            statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getPkName(final String tableName) {
        if (seqTables.contains(tableName.toLowerCase())) {
            return "seq";
        }
        return "id";
    }
}
