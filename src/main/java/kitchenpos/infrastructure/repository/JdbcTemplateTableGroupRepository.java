package kitchenpos.infrastructure.repository;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTemplateTableGroupRepository implements TableGroupRepository {
    private static final String TABLE_NAME = "table_group";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final OrderTableRepository orderTableRepository;

    public JdbcTemplateTableGroupRepository(final DataSource dataSource,
                                            final OrderTableRepository orderTableRepository) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
        this.orderTableRepository = orderTableRepository;
    }

    @Override
    public TableGroup save(final TableGroup entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        updateOrderTableGrouping(entity.getOrderTables(), key.longValue());
        return select(key.longValue());
    }

    private void updateOrderTableGrouping(final List<OrderTable> orderTables, final Long tableGroupId) {
        for (final OrderTable savedOrderTable : orderTables) {
            savedOrderTable.joinTableGroup(tableGroupId);
            orderTableRepository.save(savedOrderTable);
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
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private TableGroup toEntity(final ResultSet resultSet) throws SQLException {
        final long tableGroupId = resultSet.getLong(KEY_COLUMN_NAME);
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        return new TableGroup(
                tableGroupId,
                resultSet.getObject("created_date", LocalDateTime.class),
                orderTables
        );
    }
}
