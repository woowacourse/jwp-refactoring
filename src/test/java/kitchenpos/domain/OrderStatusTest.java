package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderStatusTest {

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void valueOfSuccessTest(final String status) {
        assertThat(OrderStatus.valueOf(status).name()).isEqualTo(status);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOK", "MIL", "COMPLETE"})
    void valueOfFailTest(final String status) {
        Assertions.assertThatThrownBy(() -> OrderStatus.valueOf(status))
                .isInstanceOf(IllegalArgumentException.class);
    }
}