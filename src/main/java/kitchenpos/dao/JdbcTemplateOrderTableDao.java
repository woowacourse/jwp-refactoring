package kitchenpos.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderTableDao implements OrderTableDao {
    private static final String TABLE_NAME = "order_table";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderTableDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
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
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty" +
                " FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public void saveAll(final OrderTables orderTables) {
        for (OrderTable orderTable : orderTables.getOrderTables()) {
            save(orderTable);
        }
    }

    private OrderTable select(final Long id) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> {
            Long tableGroupId = resultSet.getObject("table_group_id", Long.class);
            int numberOfGuests = resultSet.getInt("number_of_guests");
            boolean empty = resultSet.getBoolean("empty");
            return new OrderTable(id, tableGroupId, numberOfGuests, empty, findAllOrdersByOrderTableId(id));
        });
    }

    private List<Order> findAllOrdersByOrderTableId(final Long id) {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders WHERE order_table_id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource().addValue("id", id);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> {
            Long orderId = resultSet.getLong(KEY_COLUMN_NAME);
            Long orderTableId = resultSet.getLong("order_table_id");
            String orderStatus = resultSet.getString("order_status");
            LocalDateTime orderedTime = resultSet.getObject("ordered_time", LocalDateTime.class);
            return new Order(orderId, orderTableId, OrderStatus.valueOf(orderStatus), orderedTime,
                    findAllOrderItemByOrderId(orderId));
        });
    }

    private List<OrderLineItem> findAllOrderItemByOrderId(final Long orderId) {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_line_item WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> {
            final OrderLineItem entity = new OrderLineItem();
            entity.setSeq(resultSet.getLong("seq"));
            entity.setOrderId(resultSet.getLong("order_id"));
            entity.setMenuId(resultSet.getLong("menu_id"));
            entity.setQuantity(resultSet.getLong("quantity"));
            return entity;
        });
    }

    private void update(final OrderTable entity) {
        final String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " number_of_guests = (:numberOfGuests), empty = (:empty) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", entity.getTableGroupId())
                .addValue("numberOfGuests", entity.getNumberOfGuests())
                .addValue("empty", entity.isEmpty())
                .addValue("id", entity.getId());
        jdbcTemplate.update(sql, parameters);
    }

    private OrderTable toEntity(final ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong(KEY_COLUMN_NAME);
        Long tableGroupId = resultSet.getObject("table_group_id", Long.class);
        int numberOfGuests = resultSet.getInt("number_of_guests");
        boolean empty = resultSet.getBoolean("empty");
        return new OrderTable(id, tableGroupId, numberOfGuests, empty, findOrdersByOrderTableId(id));
    }

    private List<Order> findOrdersByOrderTableId(Long orderTableId) {
        final String sql = "SELECT id, order_table_id, order_status, ordered_time FROM orders WHERE order_table_id = (:order_table_id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("order_table_id", orderTableId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> {
            final Order entity = new Order();
            entity.setId(resultSet.getLong(KEY_COLUMN_NAME));
            entity.setOrderTableId(resultSet.getLong("order_table_id"));
            entity.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
            entity.setOrderedTime(resultSet.getObject("ordered_time", LocalDateTime.class));
            return entity;
        });
    }
}
