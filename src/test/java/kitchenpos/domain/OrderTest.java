package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "cooking", "MEAL", "meal", "COMPLETION", "Completion"})
    @DisplayName("constructor")
    void constructor(String orderStatus) {
        assertThat(new Order(1L, orderStatus, LocalDateTime.now()))
            .isInstanceOf(Order.class);
    }

    @Test
    @DisplayName("constructor - 엉뚱한 order status 문자열이 들어온 경우 예외처리")
    void constructor_IfOrderStatusFormatIsWrong_ThrowException() {
        assertThatThrownBy(() -> new Order(1L, "odd", LocalDateTime.now()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource({
        "COOKING,true", "cooking,true",
        "MEAL,true", "meal,true",
        "COMPLETION,false", "Completion,false"
    })
    void isNotCompleted(String orderStatus, boolean expected) {
        Order order = new Order(1L, orderStatus, LocalDateTime.now());

        assertThat(order.isNotCompleted()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
        "COOKING,COOKING", "COOKING,MEAL", "COOKING,COMPLETION",
        "MEAL,COOKING", "MEAL,MEAL", "MEAL,COMPLETION",
        "COMPLETION,COOKING", "COMPLETION,MEAL", "COMPLETION,COMPLETION"
    })
    void changeOrderStatus(String from, OrderStatus to) {
        Order order = new Order(1L, from, LocalDateTime.now());
        order.changeOrderStatus(to);
        assertThat(order.getOrderStatus()).isEqualTo(to.name());
    }
}
