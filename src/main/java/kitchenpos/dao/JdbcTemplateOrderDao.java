package kitchenpos.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
    private static final RowMapper<Order> ROW_MAPPER = ((rs, rowNum) -> new Order(
            rs.getLong("id"),
            toOrderTable(
                    rs.getLong("order_table_id"),
                    rs.getLong("table_group_id"),
                    rs.getInt("number_of_guests"),
                    rs.getBoolean("empty")
            ),
            rs.getString("order_status"),
            rs.getObject("ordered_time", LocalDateTime.class)
    ));

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    private static OrderTable toOrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
                                           final boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    @Override
    public Order save(final Order entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
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
        final String sql =
                "SELECT o.id, o.order_table_id, o.order_status, o.ordered_time, ot.table_group_id, ot.number_of_guests, ot.empty "
                        + "FROM orders o "
                        + "JOIN order_table ot on o.order_table_id = ot.id ";
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
        final String sql =
                "SELECT o.id, o.order_table_id, o.order_status, o.ordered_time, ot.table_group_id, ot.number_of_guests, ot.empty "
                        + "FROM orders o "
                        + "JOIN order_table ot on o.order_table_id = ot.id "
                        + "WHERE o.id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }

    private void update(final Order entity) {
        final String sql = "UPDATE orders SET order_status = (:orderStatus) WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderStatus", entity.getOrderStatus())
                .addValue("id", entity.getId());
        jdbcTemplate.update(sql, parameters);
    }
}
