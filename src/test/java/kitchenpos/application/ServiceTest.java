package kitchenpos.application;

import java.util.stream.Stream;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql(scripts = "classpath:test_data_input.sql")
public abstract class ServiceTest {

    protected OrderTable createOrderTable(
            final Long id,
            final Integer numberOfGuests,
            final Boolean empty
    ) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        if (numberOfGuests != null) {
            orderTable.setNumberOfGuests(numberOfGuests);
        }
        if (empty != null) {
            orderTable.setEmpty(empty);
        }
        return orderTable;
    }

    protected static Stream<Arguments> statusAndIdProvider() {
        return Stream.of(
                Arguments.of("조리", 3L, IllegalArgumentException.class),
                Arguments.of("식사", 4L, IllegalArgumentException.class)

        );
    }
}
