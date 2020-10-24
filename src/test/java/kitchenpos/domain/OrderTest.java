package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createOrder;
import static kitchenpos.TestObjectFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("주문 등록 시 주문테이블 등록 여부 확인")
    @Test
    void setOrderTable() {
        OrderTable orderTable = createOrderTable(false);
        Order order = Order.builder()
            .orderTable(orderTable)
            .build();

        assertAll(
            () -> assertThat(order.getOrderTable()).isEqualTo(orderTable),
            () -> assertThat(order.getOrderTable().getOrders()).containsOnly(order)
        );
    }

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

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        OrderTable orderTable = createOrderTable(false);
        Order order = createOrder(orderTable);

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
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