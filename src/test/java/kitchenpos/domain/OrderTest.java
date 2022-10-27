package kitchenpos.domain;

import static kitchenpos.application.fixture.OrderFixture.ORDER_LINE_ITEMS;
import static kitchenpos.application.fixture.OrderFixture.ORDER_TABLE_NOT_EMPTY_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문의 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        Order order = new Order(ORDER_TABLE_NOT_EMPTY_ID, ORDER_LINE_ITEMS);
        order.changeOrderStatus(OrderStatus.MEAL.name());

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문이 완료상태이면 상태변경을 할 수 없다.")
    @Test
    void changeOrderStatus_Exception_Already_Complete() {
        Order order = new Order(ORDER_TABLE_NOT_EMPTY_ID, ORDER_LINE_ITEMS);
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태는 COOKING, MEAL, COMPLETION뿐이다. 이외는 예외가 발생한다.")
    @Test
    void changeOrderStatus_Exception_Invalid_Status() {
        Order order = new Order(ORDER_TABLE_NOT_EMPTY_ID, ORDER_LINE_ITEMS);
        assertThatThrownBy(() -> order.changeOrderStatus("WRONG_STATUS"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
