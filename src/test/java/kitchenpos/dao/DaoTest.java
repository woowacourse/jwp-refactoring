package kitchenpos.dao;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@JdbcTest(includeFilters = @Filter(type = FilterType.REGEX, pattern = {"kitchenpos.dao.*"}))
public @interface DaoTest {
}
