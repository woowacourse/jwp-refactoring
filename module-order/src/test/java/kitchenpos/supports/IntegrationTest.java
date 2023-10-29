package kitchenpos.supports;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sql(value = "/truncate.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public @interface IntegrationTest {

}
