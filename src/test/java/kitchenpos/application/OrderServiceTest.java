package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.맵슐랭;
import static kitchenpos.fixture.OrderFixture.완료된_세번째테이블_주문;
import static kitchenpos.fixture.OrderFixture.조리중인_첫번째테이블_주문;
import static kitchenpos.fixture.OrderTableFixture.주문가능_테이블;
import static kitchenpos.fixture.OrderTableFixture.한명인_테이블;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        menuDao = MenuFixture.setUp().getMenuDao();
        orderDao = OrderFixture.setUp().getOrderDao();
        orderLineItemDao = OrderLineItemFixture.setUp().getOrderLineItemDao();
        orderTableDao = OrderTableFixture.setUp().getOrderTableDao();
        orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        final Order order = OrderFixture.createOrder(주문가능_테이블, OrderStatus.COOKING.name(), LocalDateTime.now());
        final OrderLineItem orderLineItem = OrderLineItemFixture.createOrderLineItem(맵슐랭, 2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final Order persistedOrder = orderService.create(order);

        assertAll(
                () -> assertThat(persistedOrder.getId()).isNotNull(),
                () -> assertThat(persistedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
                () -> assertThat(persistedOrder.getOrderTableId()).isEqualTo(주문가능_테이블)
        );
    }

    @Test
    @DisplayName("주문 항목이 없으면 예외 발생")
    void whenOrderLineItemsIsEmpty() {
        final Order order = OrderFixture.createOrder(주문가능_테이블, OrderStatus.COOKING.name(), LocalDateTime.now());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 포함할 경우 예외 발생")
    void whenInvalidMenu() {
        long invalidMenuId = 99999L;
        final Order order = OrderFixture.createOrder(주문가능_테이블, OrderStatus.COOKING.name(), LocalDateTime.now());
        final OrderLineItem orderLineItem = OrderLineItemFixture.createOrderLineItem(invalidMenuId, 2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블을 포함할 경우 예외 발생")
    void whenInvalidOrderTable() {
        long invalidOrderTable = 99999L;
        final Order order = OrderFixture.createOrder(invalidOrderTable);
        final OrderLineItem orderLineItem = OrderLineItemFixture.createOrderLineItem(맵슐랭, 2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈테이블일 경우 예외 발생")
    void whenOrderTableIsEmpty() {
        final Order order = OrderFixture.createOrder(한명인_테이블);
        final OrderLineItem orderLineItem = OrderLineItemFixture.createOrderLineItem(맵슐랭, 2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성하면 상태가 조리 상태가 된다.")
    void createOrderWithCookingStatus() {
        final Order order = OrderFixture.createOrder(주문가능_테이블, null, LocalDateTime.now());
        final OrderLineItem orderLineItem = OrderLineItemFixture.createOrderLineItem(맵슐랭, 2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        final Order actual = orderService.create(order);

        assertThat(actual.getOrderStatus())
                .isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문의 목록을 조회한다.")
    void getOrders() {
        final List<Order> expectedOrders = OrderFixture.setUp().getFixtures();

        final List<Order> orders = orderService.list();

        assertAll(
                () -> assertThat(orders).hasSameSizeAs(expectedOrders),
                () -> expectedOrders.forEach(
                        order -> assertThat(orders).usingRecursiveFieldByFieldElementComparator()
                                .usingElementComparatorIgnoringFields("orderLineItems", "orderedTime")
                                .contains(order)
                )
        );
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        final Order changeOrderStatus = new Order();
        changeOrderStatus.setOrderStatus(OrderStatus.MEAL.name());

        final Order actual = orderService.changeOrderStatus(조리중인_첫번째테이블_주문, changeOrderStatus);

        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문이 존재하지 않을 때 예외 발생")
    void whenInvalidOrder() {
        long invalidOrderId = 99999L;
        final Order changeOrderStatus = new Order();
        changeOrderStatus.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(invalidOrderId, changeOrderStatus));
    }

    @Test
    @DisplayName("주문의 상태가 완료인 상태에서 변경할 경우 예외 발생")
    void whenOrderStatusIsCompletion() {
        final Order changeOrderStatus = new Order();
        changeOrderStatus.setOrderStatus(OrderStatus.MEAL.name());

        assertThatThrownBy(() -> orderService.changeOrderStatus(완료된_세번째테이블_주문, changeOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
