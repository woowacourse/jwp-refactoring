package kitchenpos.application;

import java.util.stream.Stream;
import kitchenpos.exception.InvalidOrderStateException;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "classpath:test_data_input.sql")
public abstract class ServiceTest {

    protected static Stream<Arguments> statusAndIdProvider() {
        return Stream.of(
                Arguments.of("조리", 3L, InvalidOrderStateException.class),
                Arguments.of("식사", 4L, InvalidOrderStateException.class)

        );
    }
}
