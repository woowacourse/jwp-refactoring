package kitchenpos.dao;

import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.jdbc.Sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Sql("/truncate.sql")
@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "kitchenpos.dao.*"))
public @interface DaoTest {
}
