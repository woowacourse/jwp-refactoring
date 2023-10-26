package kitchenpos.persistence.jdbc.specific;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.persistence.dto.ProductDataDto;
import kitchenpos.persistence.jdbc.NamedParameterJdbcDataAccessor;
import kitchenpos.persistence.specific.ProductDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcProductDataAccessor extends NamedParameterJdbcDataAccessor implements ProductDataAccessor {

    private static final String TABLE_NAME = "product";
    private static final String KEY_COLUMN_NAME = "id";
    private static final RowMapper<ProductDataDto> PRODUCT_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong(KEY_COLUMN_NAME);
        final String name = rs.getString("name");
        final BigDecimal price = rs.getBigDecimal("price");
        return new ProductDataDto(id, name, price);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcProductDataAccessor(final DataSource dataSource) {
        super(dataSource);
        jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public ProductDataDto save(final ProductDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final ProductDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", entity.getId());
        mapSqlParameterSource.addValue("name", entity.getName());
        mapSqlParameterSource.addValue("price", entity.getPrice());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<ProductDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<ProductDataDto> findAll() {
        final String sql = "SELECT id, name, price FROM product";
        return namedParameterJdbcTemplate.query(sql, PRODUCT_DATA_DTO_ROW_MAPPER);
    }

    private ProductDataDto select(final Long id) {
        final String sql = "SELECT id, name, price FROM product WHERE id = (:id)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(sql, parameters, PRODUCT_DATA_DTO_ROW_MAPPER);
    }
}
