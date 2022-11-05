package kitchenpos.domain.order;

import static kitchenpos.domain.DomainTestFixture.getTestMenu;
import static kitchenpos.domain.DomainTestFixture.getTestOrderTable1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.table.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        final LocalDateTime startTime = LocalDateTime.now();
        final Menu testMenu = getTestMenu();
        final OrderLineItem orderLineItem = new OrderLineItem(
                testMenu.getName(),
                testMenu.getPrice(),
                testMenu.getId(),
                1L
        );

        final OrderTable testOrderTable1 = getTestOrderTable1();
        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));

        assertAll(
                () -> assertThat(order.getOrderedTime()).isBefore(LocalDateTime.now()),
                () -> assertThat(order.getOrderedTime()).isAfter(startTime),
                () -> assertThat(order.getOrderTableId()).isEqualTo(testOrderTable1.getId()),
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final Menu testMenu = getTestMenu();
        final OrderLineItem orderLineItem = new OrderLineItem(
                testMenu.getName(),
                testMenu.getPrice(),
                testMenu.getId(),
                1L
        );
        final OrderTable testOrderTable1 = getTestOrderTable1();
        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));

        order.changeOrderStatus(OrderStatus.MEAL);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @Test
    @DisplayName("주문이 완료 상태일 때 상태를 변경하려 하면 예외가 발생한다.")
    void changeOrderStatusAlreadyCompletion() {
        final Menu testMenu = getTestMenu();
        final OrderLineItem orderLineItem = new OrderLineItem(
                testMenu.getName(),
                testMenu.getPrice(),
                testMenu.getId(),
                1L
        );
        final OrderTable testOrderTable1 = getTestOrderTable1();
        final Order order = Order.create(testOrderTable1.getId(), List.of(orderLineItem));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalArgumentException.class);
    }
}