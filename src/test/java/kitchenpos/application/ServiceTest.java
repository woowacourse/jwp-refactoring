package kitchenpos.application;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@DataJpaTest
public class ServiceTest {
}

