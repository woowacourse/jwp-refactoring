package kitchenpos.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuProductDao implements MenuProductDao {
    private static final String TABLE_NAME = "menu_product";
    private static final String KEY_COLUMN_NAME = "seq";
    private static final RowMapper<MenuProduct> ROW_MAPPER = (((rs, rowNum) ->
            new MenuProduct(
                    rs.getLong("seq"),
                    toMenu(rs.getLong("menu_id"), rs.getString(7), rs.getBigDecimal(8),
                            toMenuGroup(rs.getLong(9), rs.getString(10))),
                    toProduct(rs.getLong("product_id"), rs.getString("name"), rs.getBigDecimal("price")),
                    rs.getLong("quantity")
            )));


    private static Menu toMenu(final Long id, final String name, final BigDecimal price, final MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup);
    }

    private static MenuGroup toMenuGroup(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    private static Product toProduct(final Long id, final String name, final BigDecimal price) {
        return new Product(id, name, price);
    }

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuProduct> findAll() {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters, ROW_MAPPER);
    }

    private MenuProduct select(final Long id) {
        final String sql =
                "SELECT mp.seq, mp.menu_id, mp.product_id, mp.quantity, p.name, p.price, m.name, m.price, m.menu_group_id, "
                        + "(select name from menu_group where m.menu_group_id = id) "
                        + "FROM menu_product mp "
                        + "JOIN Product p on mp.product_id = p.id "
                        + "JOIN Menu m on mp.menu_id = m.id "
                        + "WHERE mp.seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }
}
