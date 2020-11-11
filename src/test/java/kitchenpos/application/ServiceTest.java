package kitchenpos.application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/test_data.sql")
public interface ServiceTest {
}
