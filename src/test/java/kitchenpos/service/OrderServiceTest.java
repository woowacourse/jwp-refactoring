package kitchenpos.service;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.dto.OrderStatusUpdateRequest;
import kitchenpos.util.FakeMenuDao;
import kitchenpos.util.FakeOrderDao;
import kitchenpos.util.FakeOrderLineItemDao;
import kitchenpos.util.FakeOrderTableDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderServiceTest {

    private final MenuDao menuDao = new FakeMenuDao();
    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderLineItemDao orderLineItemDao = new FakeOrderLineItemDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    @DisplayName("주문을 생성한다")
    @Test
    void create() {
        preprocessWhenCreate(
                new OrderTable(4L, null, 4, false),
                List.of(new OrderLineItem(null, 2L, 3L),
                        new OrderLineItem(null, 1L, 2L)),
                List.of(new Menu(1L, "test", 1L, null, null),
                        new Menu(2L, "test", 1L, null, null)));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(1L, 2L)));

        OrderResponse order = orderService.create(orderCreateRequest);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(order.getOrderTableId()).isEqualTo(4L),
                () -> assertThat(order.getOrderLineItems().size()).isEqualTo(2)
        );
    }
    @DisplayName("주문내역이 없는 주문을 생성할 수 없다")
    @Test
    void create_orderLineItemEmpty() {
        preprocessWhenCreate(new OrderTable(4, false), List.of(), List.of());
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L, List.of());

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("없는 메뉴를 주문내역으로 하는 주문을 생성할 수 없다")
    @Test
    void create_menuIdNotExist() {
        preprocessWhenCreate(
                new OrderTable(4, false),
                List.of(new OrderLineItem(null, 2L, 3L),
                        new OrderLineItem(null, 100L, 2L)),
                List.of(new Menu(1L, "test", 1L, null, null),
                        new Menu(2L, "test", 1L, null, null)));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(100L, 2L)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("주문테이블이 없는 주문을 생성할 수 없다")
    @Test
    void create_OrderTableEmpty() {
        preprocessWhenCreate(
                new OrderTable(2L, null, 4, false),
                List.of(new OrderLineItem(null, 2L, 3L),
                        new OrderLineItem(null, 1L, 2L)),
                List.of(new Menu(1L, "test", 1L, null, null),
                        new Menu(2L, "test", 1L, null, null)));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L,
                List.of(new OrderLineItemRequest(2L, 3L),
                        new OrderLineItemRequest(1L, 2L)));

        assertThatThrownBy(() -> orderService.create(orderCreateRequest)).isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("주문목록을 조회한다")
    @Test
    void list() {
        preprocessWhenList(2);
        List<OrderResponse> orders = orderService.list();

        assertThat(orders.size()).isEqualTo(2);
    }
    @DisplayName("주문상태를 바꾼다")
    @Test
    void changeOrderStatus() {
        preprocessWhenChange(
                new Order(1L, 2L, OrderStatus.MEAL, LocalDateTime.now(), null),
                List.of(
                        new OrderLineItem(1L, 1L, 1L),
                        new OrderLineItem(1L, 2L, 1L)));
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("COMPLETION");

        OrderResponse order = orderService.changeOrderStatus(1L, orderStatusUpdateRequest);

        assertAll(
                () -> assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
                () -> assertThat(order.getOrderTableId()).isEqualTo(2L),
                () -> assertThat(order.getOrderLineItems().size()).isEqualTo(2)
        );
    }
    @DisplayName("없는 주문의 상태를 바꿀 수 없다")
    @Test
    void changeOrderStatus_orderNotExist() {
        preprocessWhenChange(new Order(1L, OrderStatus.COOKING, LocalDateTime.now()), List.of());
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(100L, orderStatusUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("완료된 주문의 상태를 바꿀 수 없다")
    @Test
    void changeOrderStatus_stateComplete() {
        preprocessWhenChange(new Order(4L, OrderStatus.COMPLETION, LocalDateTime.now()),
                List.of(new OrderLineItem(4L, 1L, 1L)));
        OrderStatusUpdateRequest orderStatusUpdateRequest = new OrderStatusUpdateRequest("MEAL");

        assertThatThrownBy(() -> orderService.changeOrderStatus(4L, orderStatusUpdateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void preprocessWhenCreate(OrderTable orderTable, List<OrderLineItem> orderLineItems, List<Menu> menus) {
        orderTableDao.save(orderTable);
        orderLineItems.forEach(orderLineItemDao::save);
        menus.forEach(menuDao::save);
    }

    private void preprocessWhenList(int count) {
        for (int i = 0; i < count; i++) {
            orderDao.save(new Order((long) (i + 1), OrderStatus.COOKING, null));
        }
    }

    private void preprocessWhenChange(Order order, List<OrderLineItem> orderLineItems) {
        orderDao.save(order);
        orderLineItems.forEach(orderLineItemDao::save);
    }
}
