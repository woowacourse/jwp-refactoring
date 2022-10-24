package kitchenpos.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JdbcTest(includeFilters = @Filter(classes = {Repository.class, Service.class}))
public @interface ServiceTest {
}
