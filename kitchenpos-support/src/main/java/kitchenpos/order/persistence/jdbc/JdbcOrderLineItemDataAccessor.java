package kitchenpos.order.persistence.jdbc;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.infrastructure.NamedParameterJdbcDataAccessor;
import kitchenpos.order.persistence.OrderLineItemDataAccessor;
import kitchenpos.order.persistence.dto.OrderLineItemDataDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderLineItemDataAccessor extends NamedParameterJdbcDataAccessor implements OrderLineItemDataAccessor {

    private static final String TABLE_NAME = "order_line_item";
    private static final String KEY_COLUMN_NAME = "seq";
    private static final RowMapper<OrderLineItemDataDto> ORDER_LINE_ITEM_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final Long seq = rs.getLong(KEY_COLUMN_NAME);
        final Long orderId = rs.getLong("order_id");
        final Long menuId = rs.getLong("menu_id");
        final long quantity = rs.getLong("quantity");
        return new OrderLineItemDataDto(seq, orderId, menuId, quantity);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcOrderLineItemDataAccessor(final DataSource dataSource) {
        super(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public OrderLineItemDataDto save(final OrderLineItemDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final OrderLineItemDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("seq", entity.getSeq());
        mapSqlParameterSource.addValue("order_id", entity.getOrderId());
        mapSqlParameterSource.addValue("menu_id", entity.getMenuId());
        mapSqlParameterSource.addValue("quantity", entity.getQuantity());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<OrderLineItemDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderLineItemDataDto> findAll() {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_line_item";
        return namedParameterJdbcTemplate.query(sql, ORDER_LINE_ITEM_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public List<OrderLineItemDataDto> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_line_item WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId);

        return namedParameterJdbcTemplate.query(sql, parameters, ORDER_LINE_ITEM_DATA_DTO_ROW_MAPPER);
    }

    private OrderLineItemDataDto select(final Long id) {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_line_item WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, ORDER_LINE_ITEM_DATA_DTO_ROW_MAPPER);
    }
}
