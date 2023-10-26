package kitchenpos.persistence.jdbc.specific;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.persistence.dto.OrderDataDto;
import kitchenpos.persistence.jdbc.NamedParameterJdbcDataAccessor;
import kitchenpos.persistence.specific.OrderDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderDataAccessor extends NamedParameterJdbcDataAccessor implements OrderDataAccessor {

    private static final String TABLE_NAME = "orders";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<OrderDataDto> ORDER_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong(KEY_COLUMN_NAME);
        final long tableId = rs.getLong("order_table_id");
        final String orderStatus = rs.getString("order_status");
        final LocalDateTime orderedTime = rs.getObject("ordered_time", LocalDateTime.class);
        return new OrderDataDto(id, tableId, orderStatus, orderedTime);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcOrderDataAccessor(final DataSource dataSource) {
        super(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public OrderDataDto save(final OrderDataDto entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = generateParameters(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    private SqlParameterSource generateParameters(final OrderDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("order_status", entity.getOrderStatus());
        mapSqlParameterSource.addValue("ordered_time", entity.getOrderedTime());
        mapSqlParameterSource.addValue("order_table_id", entity.getOrderTableId());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<OrderDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderDataDto> findAll() {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders";
        return jdbcTemplate.query(sql, ORDER_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
                " FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderTableId", orderTableId)
                .addValue("orderStatuses", orderStatuses);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
                " FROM orders WHERE order_table_id IN (:orderTableIds) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderTableIds", orderTableIds)
                .addValue("orderStatuses", orderStatuses);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    private OrderDataDto select(final Long id) {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, ORDER_DATA_DTO_ROW_MAPPER);
    }

    private void update(final OrderDataDto entity) {
        final String sql = "UPDATE orders SET order_status = (:orderStatus) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderStatus", entity.getOrderStatus())
                .addValue("id", entity.getId());
        namedParameterJdbcTemplate.update(sql, parameters);
    }
}
