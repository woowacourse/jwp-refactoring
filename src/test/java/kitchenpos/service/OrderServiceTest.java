package kitchenpos.service;

import kitchenpos.application.MenuService;
import kitchenpos.application.OrderService;
import kitchenpos.dao.*;
import kitchenpos.domain.history.MenuHistory;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.dto.*;
import kitchenpos.util.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class OrderServiceTest {

    private final MenuDao menuDao = new FakeMenuDao();
    private final OrderDao orderDao = new FakeOrderDao();
    private final OrderLineItemDao orderLineItemDao = new FakeOrderLineItemDao();
    private final OrderTableDao orderTableDao = new FakeOrderTableDao();
    private final MenuHistoryDao menuHistoryDao = new FakeMenuHistoryDao();
    private final OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao, menuHistoryDao);
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

    @DisplayName("메뉴의 가격과 이름을 바꿔도 주문의 내역은 변동이 없어야 한다")
    @ParameterizedTest
    @CsvSource(value = {"초밥세트:8000", "밥초세트:8000", "초밥세트:7000"}, delimiter = ':')
    void notAffectedByMenuChange(String name, Long price) {
        preprocessWhenCreate(
                new OrderTable(4L, null, 4, false),
                List.of(new OrderLineItem(null, 1L, 1L)),
                List.of(new Menu(1L, "초밥세트", 8000L, null, null)));
        menuHistoryDao.save(new MenuHistory(1L, 8000L, "초밥세트", LocalDateTime.now()));
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(4L,
                List.of(new OrderLineItemRequest(1L, 1L)));
        OrderResponse orderResponse = orderService.create(orderCreateRequest);

        doChange(1L, price, name, 8000L, "초밥세트");
        OrderInfoSearchResponse orderInfoSearchResponse = orderService.getInfo(new OrderInfoSearchRequest(orderResponse.getId()));

        assertAll(
                () -> assertThat(orderInfoSearchResponse.getOrderInfo().size()).isEqualTo(1),
                () -> assertThat(orderInfoSearchResponse.getOrderInfo().get(0).getMenuName()).isEqualTo("초밥세트"),
                () -> assertThat(orderInfoSearchResponse.getOrderInfo().get(0).getPrice()).isEqualTo(8000L),
                () -> assertThat(orderInfoSearchResponse.getOrderInfo().get(0).getQuantity()).isEqualTo(1L)
        );
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

    private void doChange(Long menuId, Long newPrice, String newName, Long oldPrice, String oldName) {
        MenuService menuService = new MenuService(menuDao, new FakeMenuGroupDao(), new FakeMenuProductDao(), new FakeProductDao(), new FakeMenuHistoryDao());
        if (!newName.equals(oldName)) {
            MenuNameChangeRequest menuNameChangeRequest = new MenuNameChangeRequest(menuId, newName, LocalDateTime.now());
            menuService.changMenuName(menuNameChangeRequest);
        }
        if (!Objects.equals(newPrice, oldPrice)) {
            MenuPriceChangeRequest menuPriceChangeRequest = new MenuPriceChangeRequest(menuId, newPrice, LocalDateTime.now());
            menuService.changMenuPrice(menuPriceChangeRequest);
        }
    }
}
