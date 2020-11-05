package kitchenpos.application;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/truncate.sql")
public class AbstractServiceTest {
    @Autowired
    protected DataSource dataSource;
}
