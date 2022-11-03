package kitchenpos.product.repository;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateProductDao implements ProductDao {
    private static final String TABLE_NAME = "product";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = (rs, rowNum) -> new Product(
            rs.getLong(KEY_COLUMN_NAME),
            rs.getString("name"),
            rs.getBigDecimal("price")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME)
        ;
    }

    @Override
    public Product save(final Product entity) {
        final SqlParameterSource parameters = new BeanPropertySqlParameterSource(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
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

    @Override
    public long countProductInIds(final List<Long> ids) {
        final String sql = "SELECT COUNT(*) FROM product WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("ids", ids);
        return jdbcTemplate.queryForObject(sql, parameters, Long.class);
    }

    private Product select(final Long id) {
        final String sql = "SELECT id, name, price FROM product WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);
        return jdbcTemplate.queryForObject(sql, parameters, PRODUCT_ROW_MAPPER);
    }
}
