package kitchenpos.support;

import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(value = "/clean.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ServiceIntegrationTest {

}
