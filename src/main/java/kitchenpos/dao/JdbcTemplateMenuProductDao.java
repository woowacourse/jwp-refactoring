package kitchenpos.dao;

import kitchenpos.dto.MenuProductDto;
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
public class JdbcTemplateMenuProductDao implements MenuProductDao {
    private static final String TABLE_NAME = "menu_product";
    private static final String KEY_COLUMN_NAME = "seq";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public MenuProductDto save(final MenuProductDto entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<MenuProductDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuProductDto> findAll() {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<MenuProductDto> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private MenuProductDto select(final Long id) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private MenuProductDto toEntity(final ResultSet resultSet) throws SQLException {
        final MenuProductDto entity = new MenuProductDto();
        entity.setSeq(resultSet.getLong(KEY_COLUMN_NAME));
        entity.setMenuId(resultSet.getLong("menu_id"));
        entity.setProductId(resultSet.getLong("product_id"));
        entity.setQuantity(resultSet.getLong("quantity"));
        return entity;
    }
}
