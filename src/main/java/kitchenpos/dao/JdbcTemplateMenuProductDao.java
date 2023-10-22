package kitchenpos.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Quantity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuProductDao implements MenuProductDao {

    private static final String KEY_COLUMN_NAME = "seq";
    private static final RowMapper<MenuProduct> MENU_PRODUCT_ROW_MAPPER = (resultSet, rowNumber) -> new MenuProduct(
            resultSet.getLong(KEY_COLUMN_NAME),
            resultSet.getLong("menu_id"),
            resultSet.getLong("product_id"),
            Quantity.valueOf(resultSet.getLong("quantity"))
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateMenuProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        String sql = "INSERT INTO menu_product (quantity, menu_id, product_id) VALUES (:quantity, :menuId, :productId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("quantity", entity.getQuantityValue())
                .addValue("menuId", entity.getMenuId())
                .addValue("productId", entity.getProductId());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, parameters, keyHolder);
        return select(Objects.requireNonNull(keyHolder.getKey()).longValue());
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
        return jdbcTemplate.query(sql, MENU_PRODUCT_ROW_MAPPER);
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters, MENU_PRODUCT_ROW_MAPPER);
    }

    private MenuProduct select(final Long id) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, MENU_PRODUCT_ROW_MAPPER);
    }
}
