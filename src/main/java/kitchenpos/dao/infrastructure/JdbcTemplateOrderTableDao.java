package kitchenpos.dao.infrastructure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateOrderTableDao implements OrderTableDao {
    private static final String TABLE_NAME = "order_table";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateOrderTableDao(DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public OrderTable save(OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
            Number key = jdbcInsert.executeAndReturnKey(parameters);
            return select(key.longValue());
        }
        update(entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        try {
            return Optional.of(select(id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderTable> findAll() {
        String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table";
        return jdbcTemplate.query(sql, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        String sql = "SELECT id, table_group_id, number_of_guests, empty" +
                " FROM order_table WHERE table_group_id = (:tableGroupId)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private OrderTable select(Long id) {
        String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private void update(OrderTable entity) {
        String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " number_of_guests = (:numberOfGuests), empty = (:empty) WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", entity.getTableGroupId())
                .addValue("numberOfGuests", entity.getNumberOfGuests())
                .addValue("empty", entity.isEmpty())
                .addValue("id", entity.getId());
        jdbcTemplate.update(sql, parameters);
    }

    private OrderTable toEntity(ResultSet resultSet) throws SQLException {
        return new OrderTable(
                resultSet.getLong(KEY_COLUMN_NAME),
                isEmpty(resultSet.getLong("table_group_id")),
                resultSet.getInt("number_of_guests"),
                resultSet.getBoolean("empty")
        );
    }

    private Long isEmpty(Long tableGroupId) {
        if (tableGroupId.equals(0L)) {
            return null;
        }
        return tableGroupId;
    }
}
