package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Long orderTableId;
    private OrderLineItems orderLineItems;


    @BeforeEach
    void setUp() {
        orderTableId = 1L;
    }

    @Test
    void testOrderCreation() {
        // when
        Order order = new Order(orderTableId, orderLineItems);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
        assertThat(order.getOrderTableId().getId()).isEqualTo(orderTableId);
        assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems);
    }


    @Nested
    class UpdateOrderStatus {

        @Test
        void testOrderStatusUpdateToMeal() {
            Order order = new Order(orderTableId, orderLineItems);
            order.updateOrderStatus(OrderStatus.MEAL);

            assertEquals(OrderStatus.MEAL, order.getOrderStatus());
        }

        @Test
        void testOrderStatusUpdateToCompletion() {
            Order order = new Order(orderTableId, orderLineItems);
            order.updateOrderStatus(OrderStatus.MEAL);  // first update to MEAL
            order.updateOrderStatus(OrderStatus.COMPLETION);  // then update to COMPLETION

            assertEquals(OrderStatus.COMPLETION, order.getOrderStatus());
        }

        @Test
        void testInvalidOrderStatusUpdate() {
            Order order = new Order(orderTableId, orderLineItems);

            assertThrows(IllegalArgumentException.class, () -> order.updateOrderStatus(OrderStatus.COMPLETION));
        }

    }

}
