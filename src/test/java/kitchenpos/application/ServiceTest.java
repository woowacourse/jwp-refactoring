package kitchenpos.application;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "classpath:test_data_input.sql")
public abstract class ServiceTest {

    protected static Stream<Arguments> statusAndIdProvider() {
        return Stream.of(
                Arguments.of("조리", 3L, IllegalArgumentException.class),
                Arguments.of("식사", 4L, IllegalArgumentException.class)

        );
    }

    protected <T> T getRequest(final Class<T> requestClass, final Object... input)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final Constructor<T> defaultConstructor = requestClass.getDeclaredConstructor();
        defaultConstructor.setAccessible(true);
        final T request = defaultConstructor.newInstance();
        final Field[] fields = requestClass.getDeclaredFields();
        for (int i = 0; i < input.length; i++) {
            fields[i].setAccessible(true);
            fields[i].set(request, input[i]);
        }
        return request;
    }
}
