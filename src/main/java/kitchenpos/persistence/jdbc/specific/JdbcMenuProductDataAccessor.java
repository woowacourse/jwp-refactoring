package kitchenpos.persistence.jdbc.specific;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import kitchenpos.persistence.dto.MenuProductDataDto;
import kitchenpos.persistence.jdbc.NamedParameterJdbcDataAccessor;
import kitchenpos.persistence.specific.MenuProductDataAccessor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMenuProductDataAccessor extends NamedParameterJdbcDataAccessor implements MenuProductDataAccessor {

    private static final String TABLE_NAME = "menu_product";
    private static final String KEY_COLUMN_NAME = "seq";
    private static final RowMapper<MenuProductDataDto> MENU_PRODUCT_DATA_DTO_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong(KEY_COLUMN_NAME);
        final long menuId = rs.getLong("menu_id");
        final long productId = rs.getLong("product_id");
        final long quantity = rs.getLong("quantity");
        return new MenuProductDataDto(id, menuId, productId, quantity);
    };

    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMenuProductDataAccessor(final DataSource dataSource) {
        super(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_COLUMN_NAME);
    }

    @Override
    public MenuProductDataDto save(final MenuProductDataDto entity) {
        final SqlParameterSource parameters = generateParameters(entity);
        final Number key = jdbcInsert.executeAndReturnKey(parameters);
        return select(key.longValue());
    }

    private SqlParameterSource generateParameters(final MenuProductDataDto entity) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("seqc", entity.getSeq());
        mapSqlParameterSource.addValue("menu_id", entity.getMenuId());
        mapSqlParameterSource.addValue("product_id", entity.getProductId());
        mapSqlParameterSource.addValue("quantity", entity.getQuantity());
        return mapSqlParameterSource;
    }

    @Override
    public Optional<MenuProductDataDto> findById(final Long id) {
        try {
            return Optional.of(select(id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<MenuProductDataDto> findAll() {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product";
        return namedParameterJdbcTemplate.query(sql, MENU_PRODUCT_DATA_DTO_ROW_MAPPER);
    }

    @Override
    public List<MenuProductDataDto> findAllByMenuId(final Long menuId) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE menu_id = (:menuId)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("menuId", menuId);
        return namedParameterJdbcTemplate.query(sql, parameters, MENU_PRODUCT_DATA_DTO_ROW_MAPPER);
    }

    private MenuProductDataDto select(final Long id) {
        final String sql = "SELECT seq, menu_id, product_id, quantity FROM menu_product WHERE seq = (:seq)";
        final SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("seq", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters,
                MENU_PRODUCT_DATA_DTO_ROW_MAPPER);
    }
}
