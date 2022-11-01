package kitchenpos.infrastructure.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.vo.Price;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuRepository implements MenuRepository {
    private static final String TABLE_NAME = "menu";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final MenuProductRepository menuProductRepository;

    public JdbcTemplateMenuRepository(final DataSource dataSource, final MenuProductRepository menuProductRepository) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
        this.menuProductRepository = menuProductRepository;
    }

    @Override
    public Menu save(final Menu entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        saveMenuProducts(entity, key);
        return select(key.longValue());
    }

    private void saveMenuProducts(final Menu entity, final Number key) {
        entity.getMenuProducts().forEach(menuProduct -> {
            final MenuProduct saveMenuProduct = new MenuProduct(
                    key.longValue(),
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
            );
            menuProductRepository.save(saveMenuProduct);
        });
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
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
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
        return jdbcTemplate.queryForObject(sql, parameters,
                (resultSet, rowNumber) -> toEntity(resultSet)
        );
    }

    private Menu toEntity(final ResultSet resultSet) throws SQLException {
        final long menuId = resultSet.getLong("id");
        final List<MenuProduct> menuProducts = menuProductRepository.findAllByMenuId(menuId);
        return new Menu(
                menuId,
                resultSet.getString("name"),
                Price.valueOf(resultSet.getBigDecimal("price")),
                resultSet.getLong("menu_group_id"),
                menuProducts
        );
    }
}
