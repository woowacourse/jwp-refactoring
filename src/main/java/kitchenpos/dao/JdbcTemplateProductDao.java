package kitchenpos.dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.domain.Money;
import kitchenpos.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateProductDao implements ProductDao {

    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (resultSet, rowNumber) -> new Product(
            resultSet.getLong(KEY_COLUMN_NAME),
            resultSet.getString("name"),
            Money.valueOf(resultSet.getBigDecimal("price"))
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcTemplateProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public Product save(final Product entity) {
        String sql = "INSERT INTO product (name, price) VALUES (:name, :price)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("price", entity.getPriceValue());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, parameters, keyHolder);
        return select(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public Optional<Product> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        final String sql = "SELECT id, name, price FROM product";
        return jdbcTemplate.query(sql, PRODUCT_ROW_MAPPER);
    }

    private Product select(final Long id) {
        final String sql = "SELECT id, name, price FROM product WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, PRODUCT_ROW_MAPPER);
    }
}
