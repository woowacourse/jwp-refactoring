package kitchenpos.support;

import java.util.Set;
import org.springframework.jdbc.core.JdbcTemplate;

public class TruncateHelper {

    private TruncateHelper() {
    }

    public static void h2Truncate(Set<String> tables, JdbcTemplate jdbcTemplate) {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");
        for (String table : tables) {
            jdbcTemplate.update("TRUNCATE TABLE " + table + " RESTART IDENTITY");
        }
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
    }
}
