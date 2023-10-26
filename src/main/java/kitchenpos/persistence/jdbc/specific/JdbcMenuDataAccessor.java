package kitchenpos.persistence.jdbc.specific;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.persistence.dto.MenuDataDto;
import kitchenpos.persistence.jdbc.NamedParameterJdbcDataAccessor;
import kitchenpos.persistence.specific.MenuDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMenuDataAccessor extends NamedParameterJdbcDataAccessor implements MenuDataAccessor {

    private static final String TABLE_NAME = "menu";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<MenuDataDto> MENU_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String name = rs.getString("name");
        final BigDecimal price = rs.getBigDecimal("price");
        final long menuGroupId = rs.getLong("menu_group_id");
        return new MenuDataDto(id, name, price, menuGroupId);
    };

    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMenuDataAccessor(final DataSource dataSource) {
        super(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public MenuDataDto save(final MenuDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = simpleJdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final MenuDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("name", entity.getName());
        mapSqlParameterSource.addValue("price", entity.getPrice());
        mapSqlParameterSource.addValue("menu_group_id", entity.getMenuGroupId());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<MenuDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuDataDto> findAll() {
        final String sql = "SELECT id, name, price, menu_group_id FROM menu ";
        return namedParameterJdbcTemplate.query(sql, MENU_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        final String sql = "SELECT COUNT(*) FROM menu WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    private MenuDataDto select(final Long id) {
        final String sql = "SELECT id, name, price, menu_group_id FROM menu WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, MENU_DATA_DTO_ROW_MAPPER);
    }
}
