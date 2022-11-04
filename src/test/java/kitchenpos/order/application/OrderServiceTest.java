package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.application.ServiceTest;
import kitchenpos.order.ui.request.OrderCreateRequest;
import kitchenpos.order.ui.request.OrderLineItemRequest;
import kitchenpos.order.ui.request.OrderStatusChangeRequest;
import kitchenpos.order.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest extends ServiceTest {

    @BeforeEach
    void setUp() {
        orderTableDao.save(new OrderTable(1L, null, 1, false));
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        final Order request = Order.of(1L,
                List.of(new OrderLineItem(1L, 1)));

        final OrderResponse actual = orderService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(actual.getOrderTableId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 포함할 경우 예외 발생")
    void whenInvalidMenu() {
        long invalidMenuId = 99999L;
        final Order request = Order.of(1L,
                Collections.singletonList(new OrderLineItem(invalidMenuId, 1L)));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블을 포함할 경우 예외 발생")
    void whenInvalidOrderTable() {
        long invalidOrderTable = 99999L;
        final Order request = Order.of(invalidOrderTable,
                Collections.singletonList(new OrderLineItem(1L, 1)));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블일 경우 예외 발생")
    void whenOrderTableIsEmpty() {
        final Order request = Order.of(3L,
                Collections.singletonList(new OrderLineItem(1L, 1)));

        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성하면 상태가 조리 상태가 된다.")
    void createOrderWithCookingStatus() {
        final Order request = Order.of(1L,
                Collections.singletonList(new OrderLineItem(1L, 1)));

        final OrderResponse actual = orderService.create(request);

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void getOrders() {
        final Order request = Order.of(1L,
                Collections.singletonList(new OrderLineItem(1L, 1)));
        final OrderResponse expected = orderService.create(request);

        final List<OrderResponse> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSize(1),
                () -> assertThat(orders.get(0).getOrderTableId()).isEqualTo(expected.getOrderTableId()),
                () -> assertThat(orders.get(0).getOrderStatus()).isEqualTo(expected.getOrderStatus())
        );
        
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final Order order = Order.of(1L, Collections.singletonList(new OrderLineItem(1L, 1L)));
        final Order savedOrder = orderDao.save(order);
        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        final OrderResponse actual = orderService.changeOrderStatus(savedOrder.getId(), request);

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문이 존재하지 않을 때 예외 발생")
    void whenInvalidOrder() {
        long invalidOrderId = 99999L;
        final OrderStatusChangeRequest request = new OrderStatusChangeRequest(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
