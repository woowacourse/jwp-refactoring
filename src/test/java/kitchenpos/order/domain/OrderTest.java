package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.fixtures.OrderFixtures;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 주문_상태를_변경한다() {
        Order order = OrderFixtures.createOrder();
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 상태_변경_시_이미_완료된_주문이면_예외를_반환한다() {
        Order completed = OrderFixtures.createOrder(OrderStatus.COMPLETION);

        Exception exception = assertThrows(IllegalStateException.class, () -> completed.changeOrderStatus(OrderStatus.MEAL));
        assertThat(exception.getMessage()).isEqualTo("주문 상태가 이미 완료되었습니다.");
    }

    @Test
    void 주문_상태가_완료가_아니면_예외를_반환한다() {
        Order order = OrderFixtures.createOrder();
        Order completed = OrderFixtures.createOrder(OrderStatus.COMPLETION);

        assertAll(
            () -> assertThrows(IllegalStateException.class, order::validateCompleted),
            () -> assertDoesNotThrow(completed::validateCompleted)
        );
    }
}