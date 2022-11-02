package kitchenpos.table.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.table.domain.OrderTable;
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
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public Long save(OrderTable entity) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        Number key = jdbcInsert.executeAndReturnKey(parameters);
        return key.longValue();
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

    @Override
    public void updateTableGroupIdAndEmpty(final Long id, final Long tableGroupId, final boolean empty) {
        String sql = "UPDATE order_table SET table_group_id = (:tableGroupId)," +
                " empty = (:empty) WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("tableGroupId", tableGroupId)
                .addValue("empty", empty)
                .addValue("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public void updateNumberOfGuests(final Long id, final int numberOfGuests) {
        String sql = "UPDATE order_table SET number_of_guests = (:numberOfGuests) WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("numberOfGuests", numberOfGuests)
                .addValue("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    @Override
    public void updateEmpty(final Long id, final boolean empty) {
        String sql = "UPDATE order_table SET empty = (:empty) WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("empty", empty)
                .addValue("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    private OrderTable select(Long id) {
        String sql = "SELECT id, table_group_id, number_of_guests, empty FROM order_table WHERE id = (:id)";
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }


    private OrderTable toEntity(ResultSet resultSet) throws SQLException {
        return new OrderTable(
                resultSet.getLong(KEY_COLUMN_NAME),
                resultSet.getObject("table_group_id", Long.class),
                resultSet.getInt("number_of_guests"),
                resultSet.getBoolean("empty")
        );
    }
}
