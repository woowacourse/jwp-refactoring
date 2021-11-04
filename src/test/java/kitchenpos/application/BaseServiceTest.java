package kitchenpos.application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/data-initialization-h2.sql")
@SpringBootTest
public class BaseServiceTest {
}
