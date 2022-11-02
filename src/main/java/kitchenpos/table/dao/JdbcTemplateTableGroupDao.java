package kitchenpos.table.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.table.domain.TableGroup;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateTableGroupDao implements TableGroupDao {

    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateTableGroupDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public Long save(TableGroup entity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return key.longValue();
    }

    @Override
    public Optional<TableGroup> findById(Long id) {
        try {
            return Optional.of(select(id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TableGroup> findAll() {
        String sql = "SELECT id, created_date FROM table_group";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup select(Long id) {
        String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup toEntity(ResultSet resultSet) throws SQLException {
        return new TableGroup(
                resultSet.getLong(KEY_COLUMN_NAME),
                resultSet.getObject("created_date", LocalDateTime.class)
        );
    }
}
