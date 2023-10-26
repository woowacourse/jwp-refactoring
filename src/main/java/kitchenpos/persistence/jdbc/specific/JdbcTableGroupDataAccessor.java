package kitchenpos.persistence.jdbc.specific;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.persistence.dto.TableGroupDataDto;
import kitchenpos.persistence.jdbc.NamedParameterJdbcDataAccessor;
import kitchenpos.persistence.specific.TableGroupDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTableGroupDataAccessor extends NamedParameterJdbcDataAccessor implements TableGroupDataAccessor {

    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<TableGroupDataDto> TABLE_GROUP_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final Long id = rs.getLong(KEY_COLUMN_NAME);
        final LocalDateTime createdDate = rs.getObject("created_date", LocalDateTime.class);
        return new TableGroupDataDto(id, createdDate);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTableGroupDataAccessor(final DataSource dataSource) {
        super(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public TableGroupDataDto save(final TableGroupDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final TableGroupDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("created_date", entity.getCreatedDate());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<TableGroupDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TableGroupDataDto> findAll() {
        final String sql = "SELECT id, created_date FROM table_group";
        return namedParameterJdbcTemplate.query(sql, TABLE_GROUP_DATA_DTO_ROW_MAPPER);
    }

    private TableGroupDataDto select(final Long id) {
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, TABLE_GROUP_DATA_DTO_ROW_MAPPER);
    }
}
