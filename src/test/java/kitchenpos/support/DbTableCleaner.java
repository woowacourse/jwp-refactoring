package kitchenpos.support;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 모든 DB Table을 Truncate하고 자동 증가한 Sequnece를 초기화하는 역할을 합니다.
 */
@Component
public class DbTableCleaner implements InitializingBean {

    private static final String FK_RULE_FALSE_SQL = "SET REFERENTIAL_INTEGRITY FALSE";
    private static final String FK_RULE_TRUE_SQL = "SET REFERENTIAL_INTEGRITY TRUE";

    @Autowired
    private DataSource dataSource;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {

        try (Connection connection = dataSource.getConnection()) {
            extractTableNames(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void extractTableNames(Connection conn) throws SQLException {

        List<String> tableNames = new ArrayList<>();

        try (ResultSet tables = conn
                .getMetaData()
                .getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"})
        ) {
            while (tables.next()) {
                tableNames.add(tables.getString("table_name"));
            }

            this.tableNames = tableNames;
        }
    }

    public void clear() {
        try (Connection connection = dataSource.getConnection()) {
            cleanUpDatabase(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 특정 테이블의 PK에 해당하는 데이터를 삭제합니다.
     */
    public void removeRecordByPk(final String tableName, final Long pk) {

        try (Connection connection = dataSource.getConnection()) {
            removeRecordByPkInternal(connection, tableName, pk);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private void removeRecordByPkInternal(final Connection conn, final String tableName, final Long pk)
            throws SQLException {

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(FK_RULE_FALSE_SQL);
            statement.executeUpdate("DELETE " + tableName + " WHERE ID = " + pk.toString());
            statement.executeUpdate(FK_RULE_TRUE_SQL);
        }
    }


    private void cleanUpDatabase(Connection conn) throws SQLException {

        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(FK_RULE_FALSE_SQL);

            for (String tableName : tableNames) {
                try {
                    statement.executeUpdate("TRUNCATE TABLE " + tableName);
                } catch (SQLException ignore) {
                }
            }

            statement.executeUpdate(FK_RULE_TRUE_SQL);
        }
    }
}
