package kitchenpos.ordertable.persistence.jdbc;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.ordertable.persistence.OrderTableDataAccessor;
import kitchenpos.ordertable.persistence.dto.OrderTableDataDto;
import kitchenpos.support.NamedParameterJdbcDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcOrderTableDataAccessor extends NamedParameterJdbcDataAccessor implements OrderTableDataAccessor {

    private static final String TABLE_NAME = "order_table";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<OrderTableDataDto> ORDER_TABLE_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong(KEY_COLUMN_NAME);
        final Long tableGroupId = rs.getObject("table_group_id", Long.class);
        final int numberOfGuests = rs.getInt("number_of_guests");
        final boolean empty = rs.getBoolean("empty");
        return new OrderTableDataDto(id, tableGroupId, numberOfGuests, empty);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcOrderTableDataAccessor(final DataSource dataSource) {
        super(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public OrderTableDataDto save(final OrderTableDataDto entity) {
        if (Objects.isNull(entity.getId())) {
            final SqlParameterSource parameters = generateParameters(entity);
            final Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    private SqlParameterSource generateParameters(final OrderTableDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("tableGroupId", entity.getTableGroupId());
        mapSqlParameterSource.addValue("numberOfGuests", entity.getNumberOfGuests());
        mapSqlParameterSource.addValue("empty", entity.isEmpty());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<OrderTableDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderTableDataDto> findAll() {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table";
        return namedParameterJdbcTemplate.query(sql, ORDER_TABLE_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public List<OrderTableDataDto> findAllByIdIn(final List<Long> ids) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, parameters, ORDER_TABLE_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public List<OrderTableDataDto> findAllByTableGroupId(final Long tableGroupId) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty" +
                " FROM order_table WHERE table_group_id = (:tableGroupId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);

        return namedParameterJdbcTemplate.query(sql, parameters, ORDER_TABLE_DATA_DTO_ROW_MAPPER);
    }

    private OrderTableDataDto select(final Long id) {
        final String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, ORDER_TABLE_DATA_DTO_ROW_MAPPER);
    }

    private void update(final OrderTableDataDto entity) {
        final String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " number_of_guests = (:numberOfGuests), empty = (:empty) WHERE id = (:id)";
        namedParameterJdbcTemplate.update(sql, generateParameters(entity));
    }
}
