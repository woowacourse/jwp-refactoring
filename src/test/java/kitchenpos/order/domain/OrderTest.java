package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import kitchenpos.fixtures.OrderFixtures;
import kitchenpos.fixtures.TableFixtures;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    void 생성_시_주문항목이_비어있으면_예외를_반환한다() {
        List<OrderLineItem> emptyItems = Collections.emptyList();

        Exception exception = assertThrows(
            IllegalStateException.class,
            () -> new Order(TableFixtures.createOrderTable(false), emptyItems)
        );
        assertThat(exception.getMessage()).isEqualTo("주문 항목이 비어있습니다.");
    }

    @Test
    void 생성_시_주문_테이블이_빈_테이블이면_예외를_반환한다() {
        OrderTable emptyTable = TableFixtures.createOrderTable(true);

        Exception exception = assertThrows(
            IllegalStateException.class,
            () -> new Order(emptyTable, OrderFixtures.createOrderLineItems())
        );
        assertThat(exception.getMessage()).isEqualTo("빈 테이블입니다.");
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