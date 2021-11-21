package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.factory.OrderFactory;
import kitchenpos.factory.OrderLineItemFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemsTest {

    private OrderLineItems orderLineItems;

    private Long orderId;

    private Order order;

    @BeforeEach
    void setUp() {
        OrderLineItem orderLineItem1 = OrderLineItemFactory.builder()
            .seq(1L)
            .orderId(1L)
            .menuId(1L)
            .quantity(2L)
            .build();

        OrderLineItem orderLineItem2 = OrderLineItemFactory.builder()
            .seq(2L)
            .orderId(2L)
            .menuId(1L)
            .quantity(3L)
            .build();

        orderLineItems = new OrderLineItems(orderLineItem1, orderLineItem2);

        orderId = 1L;

        order = OrderFactory.builder()
            .id(orderId)
            .build();
    }

    @DisplayName("OrderLineItems 안에 있는 객체들의 Order 정보를 변경한다")
    @Test
    void updateOrderInfo() {
        // when
        orderLineItems.updateOrderInfo(order);

        // then
        assertThat(orderLineItems.toList())
            .extracting("orderId")
            .allMatch(extractedId -> extractedId.equals(orderId));
    }

    @DisplayName("OrderLineItems 안에 있는 객체들의 Order 정보 변경 실패한다 - 객체가 없는 경우")
    @Test
    void updateOrderInfoFail_whenOrderLineItemsIsEmpty() {
        // given
        orderLineItems = new OrderLineItems();

        // when
        ThrowingCallable throwingCallable = () -> orderLineItems.updateOrderInfo(order);

        // then
        assertThatThrownBy(throwingCallable)
            .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
