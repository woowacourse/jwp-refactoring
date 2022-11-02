package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.product.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    private List<OrderLineItem> getOrderLineItems(final Long menuId, final long quantity) {
        return List.of(new OrderLineItem(
                null,
                null,
                menuId,
                "마이쮸",
                BigDecimal.valueOf(800),
                quantity)
        );
    }

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
                OrderStatus.COOKING,
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
                OrderStatus.COMPLETION,
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
                OrderStatus.COOKING,
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
                OrderStatus.MEAL,
                LocalDateTime.now(),
                getOrderLineItems(1L, 1)
        );

        final boolean result = order.isStatusMeal();

        assertThat(result).isTrue();
    }
}
