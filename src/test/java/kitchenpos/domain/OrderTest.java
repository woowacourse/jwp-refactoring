package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("[예외] 빈 테이블에 대한 주문 생성")
    @Test
    void create_Fail_With_EmptyTable() {
        OrderTable orderTable = createOrderTable(true);

        assertThatThrownBy(
            () -> Order.builder()
                .orderTable(orderTable)
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus() {
        OrderTable orderTable = createOrderTable(false);
        Order order = createOrder(orderTable);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("[예외] 이미 완료된 주문의 상태 변경")
    @Test
    void changeOrderStatus_With_CompletedOrder() {
        OrderTable orderTable = createOrderTable(false);
        Order order = createOrder(orderTable);
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(
            () -> order.changeOrderStatus(OrderStatus.COMPLETION)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}