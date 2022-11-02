package kitchenpos.domain;

import static kitchenpos.application.fixture.OrderFixtures.generateOrder;
import static kitchenpos.application.fixture.OrderLineItemFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class OrderTest {

    @Test
    void order를_생성한다() {
        List<OrderLineItem> orderLineItems = List.of(
                generateOrderLineItem(1L, 1L, 1L),
                generateOrderLineItem(1L, 2L, 3L)
        );

        Order actual = new Order(1L, orderLineItems);

        assertAll(() -> {
            assertThat(actual.getOrderTableId()).isEqualTo(1L);
            assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
            assertThat(actual.getOrderLineItems()).hasSize(2);
        });
    }

    @Test
    void order를_생성할_때_orderLineItems가_null이면_예외를_던진다() {
        assertThatThrownBy(() -> new Order(1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order를_생성할_때_orderLineItems가_비어있으면_예외를_던진다() {
        assertThatThrownBy(() -> new Order(1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void orderStatus를_변경한다() {
        Order order = generateOrder(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of());

        assertDoesNotThrow(() -> order.changeOrderStatus(OrderStatus.MEAL.name()));
    }

    @Test
    void orderStatus가_COMPLETION인_경우_예외를_던진다() {
        Order order = generateOrder(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL.name()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
