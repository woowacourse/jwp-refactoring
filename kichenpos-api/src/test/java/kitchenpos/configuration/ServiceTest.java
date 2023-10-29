package kitchenpos.configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Retention(RetentionPolicy.RUNTIME)
@Sql({"/schema.sql"})
public @interface ServiceTest {
}
