package kitchenpos.table.domain.dao;

import kitchenpos.table.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateOrderTableDao implements OrderTableDao {

    private static final String TABLE_NAME = "order_table";
    private static final String KEY_COLUMN = "id";
    private static final String TABLE_GROUP_ID_COLUMN = "table_group_id";
    private static final String NUMBER_OF_GUESTS_COLUMN = "number_of_guests";
    private static final String EMPTY_COLUMN = "empty";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderTableDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderTable> findAll() {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table";
        return jdbcTemplate.query(sql, new OrderTableRowMapper());
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        return jdbcTemplate.query(sql, parameters, new OrderTableRowMapper());
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty" +
            " FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, new OrderTableRowMapper());
    }

    private OrderTable select(final Long id) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, new OrderTableRowMapper());
    }

    private void update(final OrderTable entity) {
        final String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
            " number_of_guests = (:numberOfGuests), empty = (:empty) WHERE id = (:id)";
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(entity));
    }

    private static class OrderTableRowMapper implements RowMapper<OrderTable> {

        @Override
        public OrderTable mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new OrderTable(rs.getLong(KEY_COLUMN), rs.getObject(TABLE_GROUP_ID_COLUMN, Long.class),
                rs.getInt(NUMBER_OF_GUESTS_COLUMN), rs.getBoolean(EMPTY_COLUMN));
        }
    }
}
