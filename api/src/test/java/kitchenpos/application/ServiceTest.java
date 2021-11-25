package kitchenpos.application;

import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Inherited
@Retention(RUNTIME)
@ActiveProfiles("test")
@MockitoSettings
public @interface ServiceTest {

}
