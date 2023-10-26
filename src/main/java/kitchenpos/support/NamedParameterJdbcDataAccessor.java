package kitchenpos.support;

import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class NamedParameterJdbcDataAccessor extends BaseJdbcDataAccessor {

    protected final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected NamedParameterJdbcDataAccessor(final DataSource dataSource) {
        super(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }
}
