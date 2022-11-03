package kitchenpos.domain.order;

import static kitchenpos.domain.DomainTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.DomainTestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        final LocalDateTime startTime = LocalDateTime.now();
        final OrderLineItem orderLineItem = new OrderLineItem(testMenu.getId(), 1);

        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));

        assertAll(
                () -> assertThat(order.getOrderedTime()).isBefore(LocalDateTime.now()),
                () -> assertThat(order.getOrderedTime()).isAfter(startTime),
                () -> assertThat(order.getOrderTableId()).isEqualTo(testOrderTable1.getId()),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final OrderLineItem orderLineItem = new OrderLineItem(testMenu.getId(), 1);
        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));

        order.changeOrderStatus(OrderStatus.MEAL.name());

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문이 완료 상태일 때 상태를 변경하려 하면 예외가 발생한다.")
    void changeOrderStatusAlreadyCompletion() {
        final OrderLineItem orderLineItem = new OrderLineItem(testMenu.getId(), 1);
        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION.name());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}