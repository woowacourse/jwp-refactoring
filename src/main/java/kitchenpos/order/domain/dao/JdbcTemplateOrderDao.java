package kitchenpos.order.domain.dao;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcTemplateOrderDao implements OrderDao {

    private static final String TABLE_NAME = "orders";
    private static final String KEY_COLUMN = "id";
    private static final String ORDER_TABLE_ID_COLUMN = "order_table_id";
    private static final String ORDER_STATUS_COLUMN = "order_status";
    private static final String ORDERED_TIME_COLUMN = "ordered_time";

    private static final OrderRowMapper ROW_MAPPER = new OrderRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public Order save(final Order order) {
        if (Objects.isNull(order.getId())) {
            final Number key = jdbcInsert.executeAndReturnKey(toOrderSqlParameters(order));
            return select(key.longValue());
        }
        update(order);
        return order;
    }

    private MapSqlParameterSource toOrderSqlParameters(Order order) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(KEY_COLUMN, order.getId());
        mapSqlParameterSource.addValue(ORDER_TABLE_ID_COLUMN, order.getOrderTableId());
        mapSqlParameterSource.addValue(ORDER_STATUS_COLUMN, order.getOrderStatus().name());
        mapSqlParameterSource.addValue(ORDERED_TIME_COLUMN, order.getOrderedTime());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll() {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId, final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
            " FROM orders WHERE order_table_id = (:orderTableId) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderTableId", orderTableId)
            .addValue("orderStatuses", orderStatuses);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<String> orderStatuses) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END" +
            " FROM orders WHERE order_table_id IN (:orderTableIds) AND order_status IN (:orderStatuses)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderTableIds", orderTableIds)
            .addValue("orderStatuses", orderStatuses);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    private Order select(final Long id) {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }

    private void update(final Order order) {
        final String sql = "UPDATE orders SET order_status = (:orderStatus) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderStatus", order.getOrderStatus())
            .addValue(KEY_COLUMN, order.getId());
        jdbcTemplate.update(sql, parameters);
    }

    private static class OrderRowMapper implements RowMapper<Order> {

        @Override
        public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Order(rs.getLong(KEY_COLUMN), rs.getLong(ORDER_TABLE_ID_COLUMN),
                OrderStatus.valueOf(rs.getString(ORDER_STATUS_COLUMN)),
                rs.getObject(ORDERED_TIME_COLUMN, LocalDateTime.class));
        }
    }
}
