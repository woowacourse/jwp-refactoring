package kitchenpos.dao;

import kitchenpos.domain.OrderMenu;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.Optional;

@Repository
public class JdbcTemplateOrderMenuDao implements OrderMenuDao {
    private static final String TABLE_NAME = "order_menu";
    private static final String KEY_COLUMN_NAME = "seq";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderMenuDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public OrderMenu save(final OrderMenu entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<OrderMenu> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderMenu> findAll() {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_menu";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderMenu> findAllByOrderId(final Long orderId) {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_menu WHERE order_id = (:orderId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("orderId", orderId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderMenu select(final Long id) {
        final String sql = "SELECT seq, order_id, menu_id, quantity FROM order_menu WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderMenu toEntity(final ResultSet resultSet) throws SQLException {
        long seq = resultSet.getLong(KEY_COLUMN_NAME);
        long orderId = resultSet.getLong("order_id");
        long menuId = resultSet.getLong("menu_id");
        long quantity = resultSet.getLong("quantity");

        return new OrderMenu(seq, orderId, menuId, quantity);
    }
}
