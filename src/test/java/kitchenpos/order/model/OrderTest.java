package kitchenpos.order.model;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import kitchenpos.order.model.Order;
import kitchenpos.order.model.OrderStatus;

class OrderTest {
    @DisplayName("주문 상태를 변경할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"COMPLETION:true", "MEAL:false", "COOKING:false"}, delimiterString = ":")
    void isComplete(OrderStatus orderStatus, boolean result) {
        Order order = new Order(null, null, orderStatus, LocalDateTime.now());

        assertThat(order.isComplete()).isEqualTo(result);
    }

    @DisplayName("주문 상태가 계산 완료인 경우 변경할 수 없다.")
    @ParameterizedTest
    @CsvSource({"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus_WithCompletionOrder_ThrownException(OrderStatus orderStatus) {
        Order order = new Order(1L, 1L, OrderStatus.COMPLETION, LocalDateTime.now());

        assertThatThrownBy(() -> order.changeOrderStatus(orderStatus))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이미 결제가 끝난 주문은 변경할 수 없습니다.");
    }
}