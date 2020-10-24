package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {

    @DisplayName("주문항목 생성 시 주문 등록 여부 확인")
    @Test
    void setOrder() {
        OrderTable orderTable = createOrderTable(false);
        Order order = createOrder(orderTable);

        OrderLineItem orderLineItem = OrderLineItem.builder()
            .order(order)
            .build();

        assertAll(
            () -> assertThat(orderLineItem.getOrder()).isEqualTo(order),
            () -> assertThat(orderLineItem.getOrder().getOrderLineItems()).containsOnly(orderLineItem)
        );
    }
}