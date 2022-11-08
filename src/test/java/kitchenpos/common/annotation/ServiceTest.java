package kitchenpos.common.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/truncate.sql")
@SpringBootTest
public class ServiceTest {
}
