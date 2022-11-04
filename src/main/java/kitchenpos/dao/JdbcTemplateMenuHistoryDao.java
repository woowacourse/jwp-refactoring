package kitchenpos.dao;

import kitchenpos.domain.history.MenuHistory;
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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JdbcTemplateMenuHistoryDao implements MenuHistoryDao {

    private static final String TABLE_NAME = "menu_history";
    private static final String KEY_COLUMN_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateMenuHistoryDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public MenuHistory save(MenuHistory entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    @Override
    public List<MenuHistory> findAllByDateAndMenuId(Long menuId, LocalDateTime searchTime) {
        final String sql = "SELECT id, menu_id, price_at_time, name_at_time, create_date FROM menu_histroy WHERE id = (:id) AND create_date <= (:searchTime)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menu_id", menuId)
                .addValue("create_date", searchTime);
        return jdbcTemplate.query(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet))
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toUnmodifiableList());
    }

    private MenuHistory select(final Long id) {
        final String sql = "SELECT id, menu_id, price_at_time, name_at_time, create_date FROM menu_histroy WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, (resultSet, rowNumber) -> toEntity(resultSet));
    }

    private MenuHistory toEntity(final ResultSet resultSet) throws SQLException {
        final MenuHistory entity = new MenuHistory(
                resultSet.getLong("id"),
                resultSet.getLong("menu_id"),
                resultSet.getLong("price_at_time"),
                resultSet.getString("name_at_time"),
                resultSet.getObject("created_date", LocalDateTime.class));
        return entity;
    }


}
