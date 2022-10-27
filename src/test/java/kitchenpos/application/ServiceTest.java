package kitchenpos.application;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kitchenpos.fixture.ServiceDependencies;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@Inherited
@SpringBootTest
@Sql("classpath:truncate.sql")
@Import(ServiceDependencies.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceTest {
}
