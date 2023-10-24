package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION;
import static kitchenpos.exception.OrderExceptionType.ORDER_TABLE_EMPTY_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.exception.BaseExceptionType;
import kitchenpos.exception.OrderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Menu menu;
    private List<OrderLineItem> orderLineItems;

    @BeforeEach
    void setUp() {
        this.menu = new Menu("신메뉴", new Price(1000), new MenuGroup("신메뉴 그룹"));
        this.orderLineItems = List.of(
                new OrderLineItem(null, menu, new Quantity(1)),
                new OrderLineItem(null, menu, new Quantity(2))
        );
    }

    @Test
    void 주문_저장시_테이블이_주문_불가능한_상태면_예외_발생() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 10, true);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_EMPTY_EXCEPTION);
    }

    @Test
    void OrderLineItem을_추가한다() {
        // given
        OrderTable orderTable = new OrderTable(null, 10, false);
        Order order = new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems);
        OrderLineItem orderLineItem = new OrderLineItem(1L, order, null, new Quantity(10));

        // when
        order.add(orderLineItem);

        // then
        assertThat(order.orderLineItems().size()).isEqualTo(3);
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 10, false);
        Order order = new Order(orderTable, COOKING, LocalDateTime.now(), orderLineItems);

        // when
        order.changeOrderStatus(COMPLETION);

        // then
        assertThat(order.orderStatus()).isEqualTo(COMPLETION);
    }

    @Test
    void 이미_주문_상태가_계산_완료이면_예외_발생() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 10, false);
        Order order = new Order(orderTable, COMPLETION, LocalDateTime.now(), orderLineItems);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                order.changeOrderStatus(COMPLETION)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_STATUS_ALREADY_COMPLETION_EXCEPTION);
    }

    @Test
    void 테이블의_empty를_변경할_때_계산_완료된_상태면_예외_발생() {
        // given
        OrderTable orderTable = new OrderTable(1L, null, 10, false);
        Order order = new Order(orderTable, COMPLETION, LocalDateTime.now(), orderLineItems);

        // when
        BaseExceptionType exceptionType = assertThrows(OrderException.class, () ->
                order.changeOrderTableEmpty(true)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_STATUS_IS_NOT_COMPLETION_EXCEPTION);
    }
}
