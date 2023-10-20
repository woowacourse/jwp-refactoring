package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.exception.OrderException.CompletionOrderException;
import kitchenpos.domain.exception.OrderException.EmptyOrderLineItemsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 생성할 수 있다. 이때 주문 상태는 cooking, 주문 시간은 주문을 생성한 시간이 된다.")
    void init_success() {
        LocalDateTime start = LocalDateTime.now();
        Order order = Order.of(new OrderTable(10), List.of(new OrderLineItem(1L, 10)));
        LocalDateTime end = LocalDateTime.now();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            softAssertions.assertThat(order.getOrderedTime()).isBetween(start, end);
        });
    }

    @Test
    @DisplayName("주문에 상품 항목이 비어있으면 주문을 생성할 수 없다.")
    void init_fail1() {
        assertThatThrownBy(() -> Order.of(new OrderTable(10), Collections.EMPTY_LIST))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문을 변경할 수 있다.")
    void changeOrderStatus_success() {
        Order order = Order.of(new OrderTable(10), List.of(new OrderLineItem(1L, 10)));
        OrderStatus lastOrderStatus = order.getOrderStatus();

        order.changeOrderStatus(OrderStatus.MEAL);
        OrderStatus newOrderStatus = order.getOrderStatus();

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(lastOrderStatus).isEqualTo(OrderStatus.COOKING);
            softAssertions.assertThat(newOrderStatus).isEqualTo(OrderStatus.MEAL);
        });
    }

    @Test
    @DisplayName("주문이 완료된 경우에는 주문을 변경할 수 없다.")
    void changeOrderStatus_fail2() {
        Order order = Order.of(new OrderTable(10), List.of(new OrderLineItem(1L, 10)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(CompletionOrderException.class);
    }
}
