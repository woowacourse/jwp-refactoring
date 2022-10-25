package kitchenpos.application;

import kitchenpos.support.DatabaseCleanerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(DatabaseCleanerExtension.class)
public class ServiceTest {
}
