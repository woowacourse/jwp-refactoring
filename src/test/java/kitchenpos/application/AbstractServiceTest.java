package kitchenpos.application;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/truncate.sql")
public class AbstractServiceTest {

}
