package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.List.of;
import static kitchenpos.fixture.OrderLineItemFixture.orderLineItem;
import static kitchenpos.fixture.OrderTableFixtrue.orderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @Test
    void 주문은_주문_항목이_하나_이상이_아니면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTable(10, false);

        // expect
        assertThatThrownBy(() -> new Order(orderTable, of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이여야 합니다");
    }

    @Test
    void 주문은_주문_테이블이_빈_테이블이면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTable(10, true);

        // expect
        assertThatThrownBy(() -> new Order(orderTable, of(orderLineItem(1L, 10))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 빈 테이블입니다");
    }

    @Test
    void 주문의_상태를_변경할_때_완료_상태면_예외가_발생한다() {
        // given
        OrderTable orderTable = orderTable(10, false);
        OrderLineItem orderLineItem = orderLineItem(1L, 10);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, List.of(orderLineItem));

        // expect
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료 상태의 주문은 변경할 수 없습니다");
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        OrderTable orderTable = orderTable(10, false);
        OrderLineItem orderLineItem = orderLineItem(1L, 10);
        Order order = new Order(orderTable, OrderStatus.COOKING, List.of(orderLineItem));

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }
}
