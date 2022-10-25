package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.RepositoryTest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class OrderServiceTest {

    private OrderService sut;
    private TableService tableService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        sut = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("주문을 등록할 수 있다. (주문을 하면 조리 상태가 된다.)")
    @Test
    void create() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

        // when
        final Order createdOrder = sut.create(order);

        // then
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getOrderStatus()).isEqualTo(COOKING.name());
        final Order foundOrder = orderDao.findById(createdOrder.getId()).get();
        assertThat(foundOrder)
                .usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .isEqualTo(createdOrder);
    }

    @DisplayName("주문을 할 때 하나 이상의 메뉴를 주문해야한다.")
    @Test
    void createOrderWithOneMenu() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of());

        // when & then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문한 메뉴 항목 개수와 실제 메뉴의 수가 일치해야한다.")
    @Test
    void orderLineItemSizeEqualToMenuSize() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(),
                invalidQuantityOrderLineItem());

        // when & then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 안된다.")
    @Test
    void createWithNonEmptyOrderTable() {
        // given
        final OrderTable orderTable = new OrderTable(1, true);
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> sut.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태는 변경될 수 있다.")
    @Test
    void canChangeOrderStatus() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
        final Order createdOrder = sut.create(order);

        // when
        final Order changeOrder = new Order(createdOrderTable.getId(), "COMPLETION", LocalDateTime.now(),
                List.of(orderLineItem));
        final Order changedOrder = sut.changeOrderStatus(createdOrder.getId(), changeOrder);

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(COMPLETION.name());
    }

    @DisplayName("주문의 조회결과가 없는 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void canNotChangeOrderStatusWhenEmptyOrder() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order = new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
        final Order changeOrder = new Order(createdOrderTable.getId(), "COMPLETION", LocalDateTime.now(),
                List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> sut.changeOrderStatus(order.getId(), changeOrder))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 이미 계산 완료이면 주문 상태를 변경할 수 없다.")
    @Test
    void canNotChangeOrderStatusWhenAlreadyCompletion() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderTable createdOrderTable = tableService.create(orderTable);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order = new Order(createdOrderTable.getId(), "COMPLETION", LocalDateTime.now(),
                List.of(orderLineItem));
        final Order savedOrder = orderDao.save(order);

        // when & then
        assertThatThrownBy(() -> sut.changeOrderStatus(savedOrder.getId(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 전체 정보를 조회할 수 있다.")
    @Test
    void list() {
        // given
        final OrderTable orderTable = new OrderTable(1, false);
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final OrderTable anotherOrderTable = new OrderTable(1, false);
        final OrderLineItem antherOrderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);

        final Order order1 = createdOrder(orderTable, orderLineItem);
        final Order order2 = createdOrder(anotherOrderTable, antherOrderLineItem);

        final Order createdOrder1 = sut.create(order1);
        final Order createdOrder2 = sut.create(order2);

        // when
        final List<Order> orders = sut.list();

        // then
        assertThat(orders)
                .hasSize(2)
                .extracting(Order::getId, Order::getOrderTableId, Order::getOrderStatus)
                .containsExactlyInAnyOrder(
                        tuple(createdOrder1.getId(), createdOrder1.getOrderTableId(), createdOrder1.getOrderStatus()),
                        tuple(createdOrder2.getId(), createdOrder2.getOrderTableId(), createdOrder2.getOrderStatus())
                );
    }

    private Order createdOrder(final OrderTable orderTable, final OrderLineItem orderLineItem) {
        final OrderTable createdOrderTable = tableService.create(orderTable);

        return new Order(createdOrderTable.getId(), LocalDateTime.now(), List.of(orderLineItem));
    }

    private static List<OrderLineItem> invalidQuantityOrderLineItem() {
        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 1L, 1L);
        return List.of(orderLineItem1, orderLineItem2);
    }
}
