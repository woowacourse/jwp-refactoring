package kitchenpos.adapter.infrastructure.jdbctemplate;

import static java.util.stream.Collectors.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

import kitchenpos.core.AggregateReference;
import kitchenpos.domain.entity.OrderTable;
import kitchenpos.domain.entity.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;

@Repository
public class JdbcTemplateTableGroupDao implements TableGroupRepository {
    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao;

    public JdbcTemplateTableGroupDao(final DataSource dataSource,
            JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
        this.jdbcTemplateOrderTableDao = jdbcTemplateOrderTableDao;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        TableGroup saved = select(key.longValue());
        saveOrderTables(saved);
        return saved;
    }

    private void saveOrderTables(TableGroup saved) {
        List<OrderTable> savedOrderTables = jdbcTemplateOrderTableDao.findAllByIdIn(saved.orderTableIds());
        final Long tableGroupId = saved.getId();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.makeTableGroup(tableGroupId);
            jdbcTemplateOrderTableDao.save(savedOrderTable);
        }
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
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup select(final Long id) {
        final String sql = "SELECT id, created_date FROM table_group WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters,
                (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup toEntity(final ResultSet resultSet) throws SQLException {
        long tableGroupId = resultSet.getLong(KEY_COLUMN_NAME);
        List<AggregateReference<OrderTable>> orderTableIds = jdbcTemplateOrderTableDao.findAllByTableGroupId(
                tableGroupId).stream()
                .map(OrderTable::getId)
                .map(AggregateReference<OrderTable>::new)
                .collect(toList());
        return new TableGroup(resultSet.getLong(KEY_COLUMN_NAME), orderTableIds, resultSet.getObject("created_date", LocalDateTime.class));
    }
}
