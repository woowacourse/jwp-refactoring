package kitchenpos.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
    private static final RowMapper<TableGroup> TABLE_GROUP_ROW_MAPPER = (resultSet, rowNumber) -> new TableGroup(
            resultSet.getLong(KEY_COLUMN_NAME),
            resultSet.getObject("created_date", LocalDateTime.class)
    );
    private static final RowMapper<OrderTable> ORDER_TABLE_ROW_MAPPER = (resultSet, rowNumber) -> new OrderTable(
            resultSet.getLong(KEY_COLUMN_NAME),
            resultSet.getObject("table_group_id", Long.class),
            resultSet.getInt("number_of_guests"),
            resultSet.getBoolean("empty")
    );

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
        return select(key.longValue());
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        try {
            return Optional.of(selectWithOrderTables(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TableGroup> findAll() {
        final String sql = "SELECT id, created_date FROM table_group";
        return jdbcTemplate.query(sql, TABLE_GROUP_ROW_MAPPER);
    }

    private TableGroup select(final Long id) {
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, TABLE_GROUP_ROW_MAPPER);
    }

    private TableGroup selectWithOrderTables(final Long id) {
        List<OrderTable> orderTables = findAllByTableGroupId(id);
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> new TableGroup(
                resultSet.getLong(KEY_COLUMN_NAME),
                resultSet.getObject("created_date", LocalDateTime.class),
                orderTables
        ));
    }

    private List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty" +
                " FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, ORDER_TABLE_ROW_MAPPER);
    }
}
