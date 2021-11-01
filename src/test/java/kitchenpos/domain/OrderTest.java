package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 생성_시_주문항목이_비어있으면_예외를_반환한다() {
        List<OrderLineItem> emptyItems = Collections.emptyList();

        assertThrows(
            IllegalStateException.class,
            () -> new Order(TableFixtures.createOrderTable(false), emptyItems)
        );
    }

    @Test
    void 생성_시_주문_테이블이_빈_테이블이면_예외를_반환한다() {
        OrderTable emptyTable = TableFixtures.createOrderTable(true);

        assertThrows(
            IllegalStateException.class,
            () -> new Order(emptyTable, OrderFixtures.createOrderLineItems())
        );
    }

    @Test
    void 주문_상태를_변경한다() {
        Order order = OrderFixtures.createOrder();
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);
    }

    @Test
    void 상태_변경_시_이미_완료된_주문이면_예외를_반환한다() {
        Order completed = OrderFixtures.createOrder(OrderStatus.COMPLETION);

        assertThrows(IllegalStateException.class, () -> completed.changeOrderStatus(OrderStatus.MEAL));
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