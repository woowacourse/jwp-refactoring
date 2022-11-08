package kitchenpos.order.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderMenus;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderMenuDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderMenuDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_menu")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public OrderMenus saveAll(final OrderMenus entity) {
        final List<OrderMenu> orderMenus = new ArrayList<>();
        for (final OrderMenu orderMenu : entity.getOrderMenus()) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(orderMenu);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            final OrderMenu savedOrderMenu = select(key.longValue());
            savedOrderMenu.setMenuId(orderMenu.getMenuId());
            orderMenus.add(savedOrderMenu);
        }
        return new OrderMenus(orderMenus);
    }

    private OrderMenu select(final long id) {
        final String sql = "SELECT id, name, price, menu_group_name FROM order_menu WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderMenu toEntity(final ResultSet resultSet) throws SQLException {
        final Long id = resultSet.getLong("id");
        final String name = resultSet.getString("name");
        final BigDecimal price = resultSet.getBigDecimal("price");
        final String menuGroupName = resultSet.getString("menu_group_name");
        return new OrderMenu(id, name, price, menuGroupName);
    }
}
