package kitchenpos.fixture;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.params.ParameterizedTest;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ParameterizedTest(name = "{displayName} {index}, args = {arguments}")
public @interface CustomParameterizedTest {

}
