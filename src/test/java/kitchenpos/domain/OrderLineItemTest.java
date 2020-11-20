package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderLineItemTest {
    @DisplayName("주문 항목을 생성할 수 있다.")
    @Test
    void constructor() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);

        assertAll(
            () -> assertThat(orderLineItem.getSeq()).isEqualTo(1L),
            () -> assertThat(orderLineItem.getOrderId()).isEqualTo(1L),
            () -> assertThat(orderLineItem.getMenuId()).isEqualTo(1L),
            () -> assertThat(orderLineItem.getQuantity()).isEqualTo(1)
        );
    }

    @DisplayName("주문 항목의 주문 번호를 변경할 수 있다.")
    @Test
    void changeOrderId() {
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);

        orderLineItem.changeOrderId(2L);

        assertThat(orderLineItem.getOrderId()).isEqualTo(2L);
    }
}
