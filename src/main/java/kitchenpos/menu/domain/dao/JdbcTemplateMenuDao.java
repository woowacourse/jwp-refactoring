package kitchenpos.menu.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateMenuDao implements MenuDao {

    private static final String TABLE_NAME = "menu";

    private static final String KEY_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PRICE_COLUMN = "price";
    private static final String MENU_GROUP_ID = "menu_group_id";

    private static final MenuRowMapper ROW_MAPPER = new MenuRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public Menu save(final Menu entity) {
        final Number key = jdbcInsert.executeAndReturnKey(toMenuSqlParameterSource(entity));
        return select(key.longValue());
    }

    private MapSqlParameterSource toMenuSqlParameterSource(Menu entity) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue(KEY_COLUMN, entity.getId());
        mapSqlParameterSource.addValue(NAME_COLUMN, entity.getName().getValue());
        mapSqlParameterSource.addValue(PRICE_COLUMN, entity.getPrice().getValue());
        mapSqlParameterSource.addValue(MENU_GROUP_ID, entity.getMenuGroupId());
        return mapSqlParameterSource;
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
        final String sql = "SELECT id, name, price, menu_group_id FROM menu WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
            .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, ROW_MAPPER);
    }

    private static class MenuRowMapper implements RowMapper<Menu> {

        @Override
        public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Menu(rs.getLong(KEY_COLUMN), Name.of(rs.getString(NAME_COLUMN)),
                Price.from(rs.getBigDecimal(PRICE_COLUMN)), rs.getObject(MENU_GROUP_ID, Long.class));
        }
    }
}
