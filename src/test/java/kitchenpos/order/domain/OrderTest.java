package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OrderTest {

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus_meal(String orderStatus) {
        // given
        Order order = createOrder();

        // when
        order.changeStatus(orderStatus);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(orderStatus);
    }

    private Order createOrder() {
        return new Order();
    }
}
