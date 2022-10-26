package kitchenpos.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Transactional
@SpringBootTest
public @interface ApplicationTest {
}
