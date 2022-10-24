package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ServiceTest
class OrderServiceTest {

    private final OrderService orderService;
    private final MenuDao menuDao;
    private final OrderDao orderDao;
    private final OrderLineItemDao orderLineItemDao;
    private final OrderTableDao orderTableDao;

    @Autowired
    public OrderServiceTest(OrderService orderService, MenuDao menuDao, OrderDao orderDao,
                            OrderLineItemDao orderLineItemDao, OrderTableDao orderTableDao) {
        this.orderService = orderService;
        this.menuDao = menuDao;
        this.orderDao = orderDao;
        this.orderLineItemDao = orderLineItemDao;
        this.orderTableDao = orderTableDao;
    }

    @DisplayName("주문을 한다")
    @Test
    void create() {
        final var menu = menuDao.save(new Menu("자장면", 5000, 1));
        final var orderLineItem = new OrderLineItem(1L, menu.getId(), 1);
        final var orderTable = orderTableDao.save(new OrderTable(1L, 1, false));
        final var expected = new Order(orderTable.getId(), null, LocalDateTime.now(), orderLineItem);

        final var actual = orderService.create(expected);
        expected.setOrderLineItems(orderLineItemDao.findAllByOrderId(actual.getId()));

        assertThat(actual.getId()).isPositive();
        assertOrderEqualsWithoutId(actual, expected);
    }

    private void assertOrderEqualsWithoutId(final Order actual, final Order expected) {
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .isEqualTo(expected);
        assertOrderLineItemsEquals(actual.getOrderLineItems(), expected.getOrderLineItems());
    }

    @DisplayName("하나 이상의 주문 항목을 포함해야 주문을 할 수 있다")
    @Test
    void createWithEmptyOrderLineItems() {
        final var order = new Order(1L, null, LocalDateTime.now(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 비어 있습니다.");
    }

    @DisplayName("주문 항목의 메뉴가 서로 달라야 주문을 할 수 있다")
    @Test
    void createWithDuplicatedMenu() {
        final var menu = menuDao.save(new Menu("자장면", 5000, 1));
        final var orderLineItems = List.of(
                new OrderLineItem(1L, menu.getId(), 1),
                new OrderLineItem(1L, menu.getId(), 1));
        final var order = new Order(1L, null, LocalDateTime.now(), orderLineItems);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 메뉴의 주문 항목이 존재합니다.");
    }

    @DisplayName("존재하는 주문 테이블이어야 주문을 할 수 있다")
    @Test
    void createWithNonExistOrderTable() {
        final var nonExistOrderTableId = 0L;

        final var menu = menuDao.save(new Menu("자장면", 5000, 1));
        final var orderLineItem = new OrderLineItem(1L, menu.getId(), 1);
        final var order = new Order(nonExistOrderTableId, null, LocalDateTime.now(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 테이블이 비어 있어야 주문을 할 수 있다")
    @Test
    void createWithEmptyOrderTable() {
        final var emptyOrderTable = orderTableDao.save(new OrderTable(1L, 1, true));

        final var menu = menuDao.save(new Menu("자장면", 5000, 1));
        final var orderLineItem = new OrderLineItem(1L, menu.getId(), 1);
        final var order = new Order(emptyOrderTable.getId(), null, LocalDateTime.now(), orderLineItem);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 비어 있습니다.");
    }

    @DisplayName("주문 테이블을 전체 조회한다")
    @Test
    void list() {
        final List<Order> expected = List.of(
                saveOrder(1, OrderStatus.MEAL, LocalDateTime.now(),
                        List.of(saveOrderLineItem(1, 1, 1),
                                saveOrderLineItem(1, 3, 4))),
                saveOrder(2, OrderStatus.COOKING, LocalDateTime.now(),
                        List.of(saveOrderLineItem(2, 1, 1),
                                saveOrderLineItem(2, 3, 4))));
        final List<Order> actual = orderService.list();

        assertAllMatches(actual, expected);
    }

    private void assertAllMatches(final List<Order> actualList, final List<Order> expectedList) {
        final int expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);
            assertOrderEquals(actual, expected);
        }
    }

    private void assertOrderEquals(final Order actual, final Order expected) {
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderLineItems")
                .isEqualTo(expected);
        assertOrderLineItemsEquals(actual.getOrderLineItems(), expected.getOrderLineItems());
    }

    private void assertOrderLineItemsEquals(final List<OrderLineItem> actualList,
                                            final List<OrderLineItem> expectedList) {
        final int expectedSize = actualList.size();
        assertThat(expectedList).hasSize(expectedSize);

        for (int i = 0; i < expectedSize; i++) {
            final var actual = actualList.get(i);
            final var expected = expectedList.get(i);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    private Order saveOrder(final long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                            final List<OrderLineItem> savedOrderLineItems) {
        final var order = orderDao.save(
                new Order(orderTableId, orderStatus.name(), orderedTime, savedOrderLineItems));

        final var orderId = order.getId();
        final var orderLineItems = orderLineItemDao.findAllByOrderId(orderId);
        order.setOrderLineItems(orderLineItems);

        return order;
    }

    private Order saveOrder(final long orderTableId, final OrderStatus orderStatus, final LocalDateTime orderedTime,
                            final OrderLineItem... savedOrderLineItems) {
        return saveOrder(orderTableId, orderStatus, orderedTime, List.of(savedOrderLineItems));
    }

    private OrderLineItem saveOrderLineItem(final long orderId, final long menuId, final long quantity) {
        return orderLineItemDao.save(new OrderLineItem(orderId, menuId, quantity));
    }

    @DisplayName("주문 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        final var expected = saveOrder(1L, OrderStatus.MEAL, LocalDateTime.now(),
                saveOrderLineItem(1, 1, 1));
        expected.setOrderStatus(OrderStatus.COOKING.name());

        final var actual = orderService.changeOrderStatus(expected.getId(), expected);

        assertOrderEquals(actual, expected);
    }

    @DisplayName("존재하는 주문이어야 주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatusWithNonExistOrder() {
        final var nonExistOrderId = 0L;

        final var order = saveOrder(1L, OrderStatus.MEAL, LocalDateTime.now(),
                saveOrderLineItem(1, 1, 1));

        assertThatThrownBy(() -> orderService.changeOrderStatus(nonExistOrderId, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 존재하지 않습니다.");
    }

    @DisplayName("이미 결제 완료된 주문은 더이상 주문 상태를 변경할 수 없다")
    @Test
    void changeOrderStatusWithCompletedOrder() {
        final var completedOrderStatus = OrderStatus.COMPLETION;

        final var order = saveOrder(1L, completedOrderStatus, LocalDateTime.now(),
                saveOrderLineItem(1, 1, 1));

        final var orderId = order.getId();
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 결제 완료된 주문입니다.");
    }
}