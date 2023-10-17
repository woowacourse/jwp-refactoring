package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.OrderIsCompletedException;
import kitchenpos.order.exception.OrderIsNotCompletedException;
import kitchenpos.order.exception.OrderLineEmptyException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableEmptyException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class OrderTest {

    @Test
    void 주문_테이블이_비었다면_주문을_생성할_때_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, true);

        // when, then
        assertThatThrownBy(() -> new Order(orderTable, null, null))
                .isInstanceOf(OrderTableEmptyException.class);
    }

    @Test
    void 주문_테이블이_비지_않았다면_주문을_생성할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);

        // when
        Order order = new Order(orderTable, null, null);

        // then
        assertThat(order.getOrderTable()).isEqualTo(orderTable);
    }

    @Test
    void 주문_상태가_완료라면_상태를_변경할_때_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, null);

        // when, then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(OrderIsCompletedException.class);
    }

    @Test
    void 주문_상태가_완료가_아니라면_상태를_변경할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, OrderStatus.COOKING, null);

        // when
        order.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    void 주문_항목들이_빈_경우_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, null, null);
        List<OrderLineItem> orderLineItems = List.of();

        // when, then
        assertThatThrownBy(() -> order.setupOrderLineItem(orderLineItems))
                .isInstanceOf(OrderLineEmptyException.class);
    }

    @Test
    void 주문_항목들이_있다면_주문_항목들을_설정할_수_있다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, null, null);
        List<OrderLineItem> orderLineItems = List.of(
                new OrderLineItem(null, null, 1L)
        );

        // when
        order.setupOrderLineItem(orderLineItems);

        // then
        assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems);
    }

    @Test
    void 주문_상태가_완료가_아니라면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, OrderStatus.COOKING, null);

        // when, then
        assertThatThrownBy(() -> order.validateOrderIsCompleted())
                .isInstanceOf(OrderIsNotCompletedException.class);
    }

    @Test
    void 주문_상태가_완료라면_예외를_던지지_않는다() {
        // given
        OrderTable orderTable = new OrderTable(null, 1, false);
        Order order = new Order(orderTable, OrderStatus.COMPLETION, null);

        // when, then
        assertThatCode(() -> order.validateOrderIsCompleted())
                .doesNotThrowAnyException();
    }
}
