package kitchenpos.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.vo.Price;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuDao implements MenuDao {
    private static final String TABLE_NAME = "menu";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert menuProductJdbcInsert;

    public JdbcTemplateMenuDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
        menuProductJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("menu_product")
                .usingGeneratedKeyColumns("seq");
    }

    @Override
    public Menu save(final Menu entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Long key = jdbcInsert.executeAndReturnKey(parameters)
                .longValue();
        for (final MenuProduct menuProduct : entity.getMenuProducts()) {
            menuProduct.setMenuId(key);
            final SqlParameterSource menuProductParameters = new BeanPropertySqlParameterSource(menuProduct);
            menuProductJdbcInsert.executeAndReturnKey(menuProductParameters);
        }
        return select(key);
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Menu> findAll() {
        final String sql = "SELECT id, name, price, menu_group_id FROM menu ";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toMenu(resultSet));
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        final String sql = "SELECT COUNT(*) FROM menu WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    private Menu select(final Long id) {
        final String sql = "SELECT id, name, price, menu_group_id FROM menu WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toMenu(resultSet));
    }

    private Menu toMenu(final ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        long menuGroupId = resultSet.getLong("menu_group_id");
        return new Menu(id, name, Price.valueOf(price), menuGroupId, findAllMenuProductsByMenuId(id, price));
    }

    public List<MenuProduct> findAllMenuProductsByMenuId(final Long menuId, final BigDecimal price) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters,
                (resultSet, rowNumber) -> toMenuProduct(Price.valueOf(price), resultSet));
    }

    private MenuProduct toMenuProduct(final Price price, final ResultSet resultSet) throws SQLException {
        final Long id = resultSet.getLong("seq");
        final Long menuId = resultSet.getLong("menu_id");
        final Long productId = resultSet.getLong("product_id");
        final Long quantity = resultSet.getLong("quantity");
        return new MenuProduct(id, menuId, productId, quantity, price);
    }
}
