package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderTest {

    private Long id = 1L;
    private Long orderTableId = 11L;
    private OrderStatus orderStatus = OrderStatus.COOKING;
    private LocalDateTime orderedTime = LocalDateTime.now();
    private OrderLineItem orderLineItem = new OrderLineItem(1L, "pasta", BigDecimal.valueOf(13000), 3L);
    private List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem);

    @Test
    void order를_생성할_수_있다() {
        Order order = createOrder(orderStatus, orderLineItems);

        Assertions.assertAll(
                () -> assertThat(order.getId()).isEqualTo(id),
                () -> assertThat(order.getOrderTableId()).isEqualTo(orderTableId),
                () -> assertThat(order.getOrderStatus()).isEqualTo(orderStatus),
                () -> assertThat(order.getOrderedTime()).isEqualTo(orderedTime),
                () -> assertThat(order.getOrderLineItems()).isEqualTo(orderLineItems)
        );
    }

    @Test
    void order_line_items가_비어있으면_예외를_반환한다() {
        List<OrderLineItem> emptyOrderLineItems = new ArrayList<>();
        assertThatThrownBy(() -> createOrder(orderStatus, emptyOrderLineItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_상태를_바꿀_수_있다() {
        Order order = createOrder(orderStatus, orderLineItems);
        OrderStatus completion = OrderStatus.COMPLETION;
        order.changeOrderStatus(completion);

        Assertions.assertAll(
                () -> assertThat(order.getOrderStatus()).isNotEqualTo(orderStatus),
                () -> assertThat(order.getOrderStatus()).isEqualTo(completion)
        );
    }

    @Test
    void 상태를_바꿀_때_완료_상태이면_예외를_던진다() {
        OrderStatus completion = OrderStatus.COMPLETION;
        Order order = createOrder(completion, orderLineItems);
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createOrder(final OrderStatus completion, final List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, completion, orderedTime, orderLineItems);
    }
}
