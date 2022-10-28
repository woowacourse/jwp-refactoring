package kitchenpos.menu.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuProductDao implements MenuProductDao {

    private static final String TABLE_NAME = "menu_product";

    private static final String KEY_COLUMN = "seq";
    private static final String MENU_ID_COLUMN = "menu_id";
    private static final String PRODUCT_ID_COLUMN = "product_id";
    private static final String QUANTITY_COLUMN = "quantity";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        final Number key = jdbcInsert.executeAndReturnKey(toMenuProductSqlSource(entity));
        return select(key.longValue());
    }

    private MapSqlParameterSource toMenuProductSqlSource(MenuProduct entity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(KEY_COLUMN, entity.getId());
        mapSqlParameterSource.addValue(MENU_ID_COLUMN, entity.getMenuId());
        mapSqlParameterSource.addValue(PRODUCT_ID_COLUMN, entity.getProductId());
        mapSqlParameterSource.addValue(QUANTITY_COLUMN, entity.getQuantity());
        return mapSqlParameterSource;
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
        return jdbcTemplate.query(sql, new MenuProductRowMapper());
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters, new MenuProductRowMapper());
    }

    private MenuProduct select(final Long id) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, new MenuProductRowMapper());
    }

    private static class MenuProductRowMapper implements RowMapper<MenuProduct> {

        @Override
        public MenuProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MenuProduct(rs.getLong(KEY_COLUMN), rs.getObject(MENU_ID_COLUMN, Long.class),
                rs.getObject(PRODUCT_ID_COLUMN, Long.class), rs.getLong(QUANTITY_COLUMN));
        }
    }
}
