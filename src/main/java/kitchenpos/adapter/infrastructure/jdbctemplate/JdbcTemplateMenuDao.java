package kitchenpos.adapter.infrastructure.jdbctemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.entity.Menu;
import kitchenpos.domain.repository.MenuRepository;

@Repository
public class JdbcTemplateMenuDao implements MenuRepository {
    private static final String TABLE_NAME = "menu";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplateMenuProductDao menuProductDao;

    public JdbcTemplateMenuDao(final DataSource dataSource,
            JdbcTemplateMenuProductDao menuProductDao) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
        this.menuProductDao = menuProductDao;
    }

    @Override
    public Menu save(final Menu entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
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
                (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private Menu toEntity(final ResultSet resultSet) throws SQLException {
        long menuId = resultSet.getLong("id");
        return new Menu(menuId, resultSet.getString("name"), resultSet.getBigDecimal("price"),
                resultSet.getLong("menu_group_id"), menuProductDao.findAllByMenuId(menuId));
    }
}
