package kitchenpos.util;

import java.util.stream.Stream;
import kitchenpos.exception.InvalidOrderStateException;
import kitchenpos.test.TestConfig;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(TestConfig.class)
@Sql(scripts = "classpath:test_data_input.sql")
public abstract class ServiceTest {

    protected static Stream<Arguments> statusAndIdProvider() {
        return Stream.of(
                Arguments.of("조리", 3L, InvalidOrderStateException.class),
                Arguments.of("식사", 4L, InvalidOrderStateException.class)
        );
    }
}
