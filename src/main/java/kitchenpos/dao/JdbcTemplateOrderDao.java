package kitchenpos.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.vo.Price;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderDao implements OrderDao {
    private static final String TABLE_NAME = "orders";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public Order save(final Order entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            saveAllOrderLineItem(key.longValue(), entity.getOrderLineItems());
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    private void saveAllOrderLineItem(final Long orderId, final List<OrderLineItem> orderLineItems) {
        for (OrderLineItem orderLineItem : orderLineItems) {
            saveOrderLineItem(orderId, orderLineItem);
        }
    }

    private void saveOrderLineItem(final Long orderId, final OrderLineItem orderLineItem) {
        final Map<String, Object> params = new HashMap<>();
        params.put("order_id", orderId);
        params.put("menu_name", orderLineItem.getMenuName());
        params.put("menu_price", orderLineItem.getMenuPrice());
        params.put("quantity", orderLineItem.getQuantity());
        jdbcTemplate.update(
                "insert into order_line_item (order_id, menu_name, menu_price, quantity) values (:order_id, :menu_name, :menu_price, :quantity)",
                params);
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
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
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
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private void update(final Order entity) {
        final String sql = "UPDATE orders SET order_status = (:orderStatus) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderStatus", entity.getOrderStatus())
                .addValue("id", entity.getId());
        jdbcTemplate.update(sql, parameters);
    }

    private Order toEntity(final ResultSet resultSet) throws SQLException {
        final Long id = resultSet.getLong(KEY_COLUMN_NAME);
        final Long orderTableId = resultSet.getLong("order_table_id");
        final String orderStatus = resultSet.getString("order_status");
        final LocalDateTime orderedTime = resultSet.getObject("ordered_time", LocalDateTime.class);
        return new Order(id, orderTableId, OrderStatus.valueOf(orderStatus), orderedTime,
                findAllOrderLineItemsByOrderId(id));
    }

    private List<OrderLineItem> findAllOrderLineItemsByOrderId(final Long orderId) {
        final String sql = "SELECT seq, order_id, menu_name, menu_price, quantity FROM order_line_item WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> {
            final Long seq = resultSet.getLong("seq");
            final String menuName = resultSet.getString("menu_name");
            final BigDecimal menuPrice = resultSet.getBigDecimal("menu_price");
            final long quantity = resultSet.getLong("quantity");
            return new OrderLineItem(seq, orderId, menuName, Price.valueOf(menuPrice), quantity);
        });
    }
}
