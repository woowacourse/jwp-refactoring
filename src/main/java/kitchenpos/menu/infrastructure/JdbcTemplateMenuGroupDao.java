package kitchenpos.menu.infrastructure;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.dao.MenuGroupDao;
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
public class JdbcTemplateMenuGroupDao implements MenuGroupDao {

    private static final String TABLE_NAME = "menu_group";

    private static final String KEY_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private static final MenuGroupRowMapper ROW_MAPPER = new MenuGroupRowMapper();


    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuGroupDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuGroup> findAll() {
        final String sql = "SELECT id, name FROM menu_group";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM menu_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    private MenuGroup select(final Long id) {
        final String sql = "SELECT id, name FROM menu_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }

    private static class MenuGroupRowMapper implements RowMapper<MenuGroup> {

        @Override
        public MenuGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MenuGroup(rs.getLong(KEY_COLUMN), rs.getString(NAME_COLUMN));
        }
    }
}
