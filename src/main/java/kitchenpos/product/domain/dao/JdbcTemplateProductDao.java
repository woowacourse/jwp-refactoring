package kitchenpos.product.domain.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateProductDao implements ProductDao {

    private static final String TABLE_NAME = "product";
    private static final String KEY_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String PRICE_COLUMN = "price";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateProductDao(final DataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE_NAME)
            .usingGeneratedKeyColumns(KEY_COLUMN);
    }

    @Override
    public Product save(final Product product) {
        final Number key = jdbcInsert.executeAndReturnKey(toSavingProductSqlParameters(product));
        return select(key.longValue());
    }

    private SqlParameterSource toSavingProductSqlParameters(Product product) {
        MapSqlParameterSource productSqlParameters = new MapSqlParameterSource();
        productSqlParameters.addValue(NAME_COLUMN, product.getName().getValue());
        productSqlParameters.addValue(PRICE_COLUMN, product.getPrice().getValue());
        return productSqlParameters;
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
        return jdbcTemplate.query(sql, new ProductRowMapper());
    }

    private Product select(final Long id) {
        final String sql = "SELECT id, name, price FROM product WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource(KEY_COLUMN, id);
        return jdbcTemplate.queryForObject(sql, parameters, new ProductRowMapper());
    }

    private static class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(rs.getLong(KEY_COLUMN), Name.of(rs.getString(NAME_COLUMN)),
                Price.from(rs.getBigDecimal(PRICE_COLUMN)));
        }
    }
}
