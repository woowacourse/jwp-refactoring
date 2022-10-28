package kitchenpos.table.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.table.domain.TableGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateTableGroupDao implements TableGroupDao {

    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN = "id";
    private static final String CREATED_DATE_COLUMN = "created_date";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateTableGroupDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN)
        ;
    }

    @Override
    public TableGroup save(final TableGroup tableGroup) {
        final Number key = jdbcInsert.executeAndReturnKey(toTableGroupSqlParameters(tableGroup));
        return select(key.longValue());
    }

    private SqlParameterSource toTableGroupSqlParameters(TableGroup tableGroup) {
        MapSqlParameterSource productSqlParameters = new MapSqlParameterSource();
        productSqlParameters.addValue(CREATED_DATE_COLUMN, tableGroup.getCreatedDate());
        return productSqlParameters;
    }

    @Override
    public Optional<TableGroup> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TableGroup> findAll() {
        final String sql = "SELECT id, created_date FROM table_group";
        return jdbcTemplate.query(sql, new TableGroupRowMapper());
    }

    private TableGroup select(final Long id) {
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, new TableGroupRowMapper());
    }

    private static class TableGroupRowMapper implements RowMapper<TableGroup> {

        @Override
        public TableGroup mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TableGroup(rs.getLong(KEY_COLUMN), rs.getObject("created_date", LocalDateTime.class));
        }
    }
}
