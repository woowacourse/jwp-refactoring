package kitchenpos.dao;

import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
public class JdbcTemplateMenuProductDao implements MenuProductDao {
    private static final String TABLE_NAME = "menu_product";
    private static final String KEY_COLUMN_NAME = "seq";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final RowMapper<MenuProduct> MENU_PRODUCT_MAPPER = (rs, rowNum) ->
            new MenuProduct(
                    rs.getLong("seq"),
                    rs.getLong("menu_id"),
                    rs.getLong("product_id"),
                    rs.getLong("quantity")
            );


    public JdbcTemplateMenuProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
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
        return jdbcTemplate.query(sql, MENU_PRODUCT_MAPPER);
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters, MENU_PRODUCT_MAPPER);
    }

    private MenuProduct select(final Long id) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, MENU_PRODUCT_MAPPER);
    }
}
