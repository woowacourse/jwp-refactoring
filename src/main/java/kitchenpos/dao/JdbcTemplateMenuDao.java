package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
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
    private static final RowMapper<Menu> ROW_MAPPER = ((rs, rowNum) -> new Menu(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getBigDecimal("price"),
            toMenuGroup(rs.getLong("menu_group_id"), rs.getString("name"))
    ));


    private static MenuGroup toMenuGroup(final Long id, final String name) {
        return new MenuGroup(id, name);
    }

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
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
        final String sql = "SELECT m.id, m.name, m.price, m.menu_group_id FROM menu m "
                + "JOIN menu_group mg on m.menu_group_id = mg.id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        final String sql = "SELECT COUNT(*) FROM menu WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    private Menu select(final Long id) {
        final String sql = "SELECT m.id, m.name, m.price, m.menu_group_id FROM menu m "
                + "JOIN menu_group mg on m.menu_group_id = mg.id WHERE m.id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }
}
