package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 5)));

        final Order persistedOrder = orderService.create(order);

        assertThat(persistedOrder.getId()).isNotNull();
    }

    @Test
    @DisplayName("주문 항목이 없으면 예외 발생")
    void whenOrderLineItemsIsEmpty() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 포함할 경우 예외 발생")
    void whenInvalidMenu() {
        long invalidMenuId = 99999L;
        final Order order = new Order(1L, null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, invalidMenuId, 4)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블을 포함할 경우 예외 발생")
    void whenInvalidOrderTable() {
        long invalidOrderTable = 99999L;
        final Order order = new Order(invalidOrderTable, null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 4)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블일 경우 예외 발생")
    void whenOrderTableIsEmpty() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, true));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(), List.of(new OrderLineItem(null, 1L, 4)));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성하면 상태가 조리 상태가 된다.")
    void createOrderWithCookingStatus() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 3)));

        final Order persistedOrder = orderService.create(order);
        final Order actual = orderService.changeOrderStatus(persistedOrder.getId(), persistedOrder);

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void getOrders() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 3)));
        orderService.create(order);

        final List<Order> orders = orderService.list();

        assertThat(orders).hasSize(1);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 3)));

        final Order persistedOrder = orderService.create(order);
        final Order orderDto = new Order();
        orderDto.setOrderStatus(OrderStatus.MEAL.name());
        final Order actual = orderService.changeOrderStatus(persistedOrder.getId(), orderDto);

        assertThat(actual.getOrderStatus()).isEqualTo("MEAL");
    }

    @Test
    @DisplayName("주문이 존재하지 않을 때 예외 발생")
    void whenInvalidOrder() {
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));
        long invalidOrderId = 99999L;

        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId,
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)));
    }

    @Test
    @DisplayName("주문의 상태가 완료인 상태에서 변경할 경우 예외 발생")
    void whenOrderStatusIsCompletion() {
        String orderStatus = OrderStatus.COMPLETION.name();
        final OrderTable orderTable = tableService.create(new OrderTable(1L, 0, false));

        final Order order = new Order(orderTable.getId(), null, LocalDateTime.now(),
                List.of(new OrderLineItem(null, 1L, 4)));
        final Order persistedOrder = orderService.create(order);
        order.setOrderStatus(orderStatus);
        orderService.changeOrderStatus(persistedOrder.getId(), order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(persistedOrder.getId(),
                new Order(orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), null)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
