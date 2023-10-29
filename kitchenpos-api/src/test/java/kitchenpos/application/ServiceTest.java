package kitchenpos.application;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql("/truncate.sql")
@Transactional
@SpringBootTest
public class ServiceTest {
}
