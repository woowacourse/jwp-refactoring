package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderStatusTest {

    @ParameterizedTest
    @CsvSource({
            "COOKING, false",
            "MEAL, false",
            "COMPLETION, true"
    })
    void isCompleted(OrderStatus orderStatus, boolean expected) {
        boolean actual = orderStatus.isCompleted();
        assertThat(actual).isEqualTo(expected);
    }
}
