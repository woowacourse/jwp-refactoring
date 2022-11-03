package kitchenpos.order.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderLineItemDao implements OrderLineItemDao {

    private static final String TABLE_NAME = "order_line_item";
    private static final String KEY_COLUMN = "seq";
    private static final String ORDER_ID_COLUMN = "order_id";
    private static final String MENU_ID_COLUMN = "menu_id";
    private static final String QUANTITY_COLUMN = "quantity";

    private static final OrderLineItemRowMapper ROW_MAPPER = new OrderLineItemRowMapper();
    private static final String MENU_NAME_COLUMN = "menu_name";
    private static final String MENU_PRICE_COLUMN = "menu_price";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderLineItemDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN);
    }

    @Override
    public OrderLineItem save(final OrderLineItem orderLineItem) {
        final Number key = jdbcInsert.executeAndReturnKey(toOrderLineItemSqlParameters(orderLineItem));
        return select(key.longValue());
    }

    private MapSqlParameterSource toOrderLineItemSqlParameters(OrderLineItem orderLineItem) {
        OrderMenu orderMenu = orderLineItem.getOrderMenu();
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(ORDER_ID_COLUMN, orderLineItem.getOrderId());
        mapSqlParameterSource.addValue(MENU_ID_COLUMN, orderLineItem.getMenuId());
        mapSqlParameterSource.addValue(MENU_NAME_COLUMN, orderMenu.getName());
        mapSqlParameterSource.addValue(MENU_PRICE_COLUMN, orderMenu.getPrice().getValue());
        mapSqlParameterSource.addValue(QUANTITY_COLUMN, orderLineItem.getQuantity());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<OrderLineItem> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderLineItem> findAll() {
        final String sql = "SELECT seq, order_id, menu_id, quantity, menu_name, menu_price  FROM order_line_item";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<OrderLineItem> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT seq, order_id, menu_id, quantity, menu_name, menu_price FROM order_line_item WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("orderId", orderId);
        return jdbcTemplate.query(sql, parameters, ROW_MAPPER);
    }

    private OrderLineItem select(final Long id) {
        final String sql = "SELECT seq, order_id, menu_id, quantity, menu_name, menu_price FROM order_line_item WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }

    private static class OrderLineItemRowMapper implements RowMapper<OrderLineItem> {

        @Override
        public OrderLineItem mapRow(ResultSet rs, int rowNum) throws SQLException {
            OrderMenu orderMenu = new OrderMenu(rs.getLong(MENU_ID_COLUMN), Name.of(rs.getString(MENU_NAME_COLUMN)),
                Price.from(rs.getBigDecimal(MENU_PRICE_COLUMN)));
            return new OrderLineItem(rs.getLong(KEY_COLUMN), rs.getLong(ORDER_ID_COLUMN), orderMenu,
                rs.getLong(QUANTITY_COLUMN));
        }
    }
}
