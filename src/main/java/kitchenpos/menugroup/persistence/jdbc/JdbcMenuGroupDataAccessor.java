package kitchenpos.menugroup.persistence.jdbc;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.infrastructure.NamedParameterJdbcDataAccessor;
import kitchenpos.menugroup.persistence.MenuGroupDataAccessor;
import kitchenpos.menugroup.persistence.dto.MenuGroupDataDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMenuGroupDataAccessor extends NamedParameterJdbcDataAccessor implements MenuGroupDataAccessor {

    private static final String TABLE_NAME = "menu_group";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<MenuGroupDataDto> MENU_GROUP_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        return new MenuGroupDataDto(id, name);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMenuGroupDataAccessor(final DataSource dataSource) {
        super(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public MenuGroupDataDto save(final MenuGroupDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final MenuGroupDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("name", entity.getName());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<MenuGroupDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuGroupDataDto> findAll() {
        final String sql = "SELECT id, name FROM menu_group";
        return namedParameterJdbcTemplate.query(sql, MENU_GROUP_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public boolean existsById(final Long id) {
        final String sql = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM menu_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Boolean.class);
    }

    private MenuGroupDataDto select(final Long id) {
        final String sql = "SELECT id, name FROM menu_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, MENU_GROUP_DATA_DTO_ROW_MAPPER);
    }
}
