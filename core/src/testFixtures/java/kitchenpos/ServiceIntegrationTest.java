package kitchenpos;

import kitchenpos.config.DatabaseClearExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(DatabaseClearExtension.class)
public abstract class ServiceIntegrationTest {

}
