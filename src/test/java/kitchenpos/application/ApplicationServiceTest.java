package kitchenpos.application;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "kitchenpos.adapter.infrastructure.persistence")
@JdbcTest
public class ApplicationServiceTest {
}
