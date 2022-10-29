package kitchenpos.domain;

import static kitchenpos.DomainFixture.getOrderLineItems;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @DisplayName("orderLineItems가 null이면 예외를 반환한다.")
    @Test
    void create_exception_orderLineItemsIsNull() {
        assertThatThrownBy(() -> new Order(1L, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("orderLineItems가 empty이면 예외를 반환한다.")
    @Test
    void create_exception_orderLineItemsIsEmpty() {
        assertThatThrownBy(() -> new Order(1L, List.of()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        final Order order = new Order(
                1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                getOrderLineItems(1L, 1)
        );

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 변경한다. - 주문 상태가 COMPLETION이면 예외를 반환한다.")
    @Test
    void changeOrderStatus_exception_orderStatusIsCompletion() {
        final Order order = new Order(
                1L,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                getOrderLineItems(1L, 1)
        );

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 COOKING인지 반환한다.")
    @Test
    void isStatusCooking() {
        final Order order = new Order(
                1L,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                getOrderLineItems(1L, 1)
        );

        final boolean result = order.isStatusCooking();

        assertThat(result).isTrue();
    }

    @DisplayName("주문 상태가 MEAL인지 반환한다.")
    @Test
    void isStatusMeal() {
        final Order order = new Order(
                1L,
                OrderStatus.MEAL.name(),
                LocalDateTime.now(),
                getOrderLineItems(1L, 1)
        );

        final boolean result = order.isStatusMeal();

        assertThat(result).isTrue();
    }
}
