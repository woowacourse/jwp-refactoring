package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @DisplayName("[예외] Order.addOrderLineItem을 통하지 않은 setOrder 접근")
    @Test
    void setOrder_Fail_NotThroughOrder() {
        Order order = new Order();
        OrderLineItem orderLineItem = OrderLineItem.builder().build();

        assertThatThrownBy(
            () -> orderLineItem.setOrder(order)
        ).isInstanceOf(IllegalStateException.class);
    }
}