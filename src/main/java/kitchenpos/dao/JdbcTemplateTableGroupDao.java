package kitchenpos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateTableGroupDao implements TableGroupDao {
    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateTableGroupDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        updateOrderTables(entity, key.longValue());
        return select(key.longValue());
    }

    private void updateOrderTables(final TableGroup entity, final Long tableGroupId) {
        for (final OrderTable orderTable : entity.getOrderTables()) {
            Map<String, Object> params = new HashMap<>();
            params.put("table_group_id", tableGroupId);
            params.put("number_of_guests", orderTable.getNumberOfGuests());
            params.put("empty", orderTable.isEmpty());
            params.put("id", orderTable.getId());

            orderTable.setTableGroupId(tableGroupId);
            jdbcTemplate.update(
                    "update order_table set table_group_id = :table_group_id, number_of_guests = :number_of_guests, "
                            + "empty = :empty where id = :id", params);
        }
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TableGroup> findAll() {
        final String sql = "SELECT id, created_date FROM table_group";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup select(final Long id) {
        List<OrderTable> orderTables = findAllOrderTableByTableGroupId(id);
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet, orderTables));
    }

    private List<OrderTable> findAllOrderTableByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> {
            Long id = resultSet.getLong(KEY_COLUMN_NAME);
            int numberOfGuests = resultSet.getInt("number_of_guests");
            boolean empty = resultSet.getBoolean("empty");
            return new OrderTable(id, tableGroupId, numberOfGuests, empty);
        });
    }

    private TableGroup toEntity(final ResultSet resultSet) throws SQLException {
        final Long id = resultSet.getLong(KEY_COLUMN_NAME);
        final LocalDateTime createdDate = resultSet.getObject("created_date", LocalDateTime.class);
        return new TableGroup(id, createdDate);
    }

    private TableGroup toEntity(final ResultSet resultSet, final List<OrderTable> orderTables) throws SQLException {
        final Long id = resultSet.getLong(KEY_COLUMN_NAME);
        final LocalDateTime createdDate = resultSet.getObject("created_date", LocalDateTime.class);
        return new TableGroup(id, createdDate, new OrderTables(orderTables));
    }
}
