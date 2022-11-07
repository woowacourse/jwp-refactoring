package kitchenpos.order.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.order.domain.OrderProduct;
import kitchenpos.order.domain.OrderProducts;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderProductDao implements OrderProductDao {

    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateOrderProductDao(final DataSource dataSource) {
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("order_product")
                .usingGeneratedKeyColumns("seq");
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public OrderProducts saveAll(final OrderProducts orderProducts) {
        final List<OrderProduct> savedOrderProducts = new ArrayList<>();
        for (final OrderProduct orderProduct : orderProducts.getOrderProducts()) {
            final SqlParameterSource parameters = new BeanPropertySqlParameterSource(orderProduct);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            savedOrderProducts.add(select(key.longValue()));
        }
        return new OrderProducts(savedOrderProducts);
    }

    private OrderProduct select(final Long seq) {
        final String sql = "SELECT seq, order_menu_id, name, price FROM order_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", seq);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderProduct toEntity(final ResultSet resultSet) throws SQLException {
        final Long seq = resultSet.getLong("seq");
        final long orderMenuId = resultSet.getLong("order_menu_id");
        final String name = resultSet.getString("name");
        final BigDecimal price = resultSet.getBigDecimal("price");
        return new OrderProduct(seq, orderMenuId, name, price);
    }
}
