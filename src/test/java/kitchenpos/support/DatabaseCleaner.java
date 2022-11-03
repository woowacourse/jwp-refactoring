package kitchenpos.support;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

    private static final String REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY ";
    private static final String TRUNCATE_QUERY = "TRUNCATE TABLE %s";

    @Autowired
    private DataSource dataSource;

    private final List<String> tableNames = new ArrayList<>();

    @PostConstruct
    public void afterPropertiesSet() {
        try (Connection connection = dataSource.getConnection();) {
            DatabaseMetaData metaData = connection.getMetaData();
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
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "FALSE").execute();
            for (String tableName : tableNames) {
                connection.prepareStatement(String.format(TRUNCATE_QUERY, tableName)).execute();
            }
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "TRUE").execute();
        } catch (SQLException e) {
            throw new IllegalArgumentException("[ERROR] TRUNCATE 동작하는데 실패했습니다.");
        }
    }
}
