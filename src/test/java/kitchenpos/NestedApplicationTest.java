package kitchenpos;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.Nested;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Retention(RetentionPolicy.RUNTIME)
@Nested
@SpringBootTest
@Sql("/truncate.sql")
public @interface NestedApplicationTest {
}
