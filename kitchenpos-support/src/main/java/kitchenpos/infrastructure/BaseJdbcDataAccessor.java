package kitchenpos.infrastructure;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class BaseJdbcDataAccessor {

    protected final JdbcTemplate jdbcTemplate;

    protected BaseJdbcDataAccessor(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
