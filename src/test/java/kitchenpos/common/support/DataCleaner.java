package kitchenpos.common.support;

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
public class DataCleaner implements InitializingBean {

    private static final String REFERENTIAL_INTEGRITY = "SET REFERENTIAL_INTEGRITY ";
    private static final String TRUNCATE = "TRUNCATE TABLE %s";
    private static final String ID_RESET = "ALTER TABLE %s ALTER COLUMN id RESTART WITH 1";

    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    @Transactional
    public void truncate() {
        try (final Connection connection = dataSource.getConnection()) {
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "FALSE")
                    .execute();
            for (String tableName : tableNames) {
                connection.prepareStatement(String.format(TRUNCATE, tableName))
                        .execute();
                connection.prepareStatement(String.format(ID_RESET, tableName))
                        .execute();
            }
            connection.prepareStatement(REFERENTIAL_INTEGRITY + "TRUE")
                    .execute();
        } catch (SQLException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void afterPropertiesSet() {
        tableNames = new ArrayList<>();
        try {
            final DatabaseMetaData metaData = dataSource.getConnection()
                    .getMetaData();
            final ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});
            while (tables.next()) {
                final String tableName = tables.getString("TABLE_NAME");
                if (tableName.contains("flyway")) {
                    continue;
                }
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            throw new NoSuchElementException();
        }
    }
}
