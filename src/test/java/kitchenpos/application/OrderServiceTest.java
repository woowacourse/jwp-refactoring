package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.util.Lists.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.inmemorydao.InMemoryMenuDao;
import kitchenpos.inmemorydao.InMemoryOrderDao;
import kitchenpos.inmemorydao.InMemoryOrderLineItemDao;
import kitchenpos.inmemorydao.InMemoryOrderTableDao;

@DisplayName("OrderService 테스트")
class OrderServiceTest {
    private MenuDao menuDao;
    private OrderDao orderDao;
    private OrderLineItemDao orderLineItemDao;
    private OrderTableDao orderTableDao;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        this.menuDao = new InMemoryMenuDao();
        this.orderDao = new InMemoryOrderDao();
        this.orderLineItemDao = new InMemoryOrderLineItemDao();
        this.orderTableDao = new InMemoryOrderTableDao();
        this.orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
    }

    @DisplayName("주문을 등록한다")
    @Test
    void create() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        // When
        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(newArrayList(orderLineItem));

        final Order createdOrder = orderService.create(order);

        // Then
        assertThat(createdOrder)
                .extracting(Order::getId)
                .isNotNull()
        ;
    }

    @DisplayName("주문할 메뉴의 목록이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void create_EmptyOrderLineItems_ExceptionThrown() {
        // Given
        final Order order = new Order();

        // Then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문할 메뉴가 존재하지 않을 경우 예외가 발생한다")
    @Test
    void create_MenuNotExists_ExceptionThrown() {
        // Given
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L); // 존재하지 않는 메뉴의 id

        final Order order = new Order();
        order.setOrderLineItems(newArrayList(orderLineItem));

        // Then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 존재하지 않을 경우 예외가 발생한다")
    @Test
    void create_OrderTableNotExists_ExceptionThrown() {
        // Given
        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderLineItems(newArrayList(orderLineItem));

        // Then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문 테이블이 빈 테이블일 경우 예외가 발생한다")
    @Test
    void create_OrderTableIsEmpty_ExceptionThrown() {
        // Given
        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Order order = new Order();
        order.setOrderLineItems(newArrayList(orderLineItem));
        order.setOrderTableId(savedOrderTable.getId());

        // Then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문의 목록을 조회한다")
    @Test
    void list() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(newArrayList(orderLineItem));

        final Order savedOrder = orderService.create(order);

        // When
        final List<Order> list = orderService.list();

        // Then
        assertAll(
                () -> assertThat(list).isNotEmpty()
                ,
                () -> assertThat(list)
                        .extracting(Order::getId)
                        .contains(savedOrder.getId())
        );
    }

    @DisplayName("주문의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(newArrayList(orderLineItem));

        final Order savedOrder = orderService.create(order);

        // When
        final Order changeStatusOrder = new Order();
        changeStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order changedOrder = orderService.changeOrderStatus(savedOrder.getId(),
                changeStatusOrder);

        // Then
        assertAll(
                () -> assertThat(changedOrder)
                        .extracting(Order::getId)
                        .isEqualTo(savedOrder.getId())
                ,
                () -> assertThat(changedOrder)
                        .extracting(Order::getOrderStatus)
                        .isEqualTo(changeStatusOrder.getOrderStatus())
        );
    }

    @DisplayName("상태를 변경할 주문이 존재하지 않는 경우 예외가 발생한다")
    @Test
    void changeOrderStatus_OrderNotExists_ExceptionThrown() {
        // Given
        final Order changeStatusOrder = new Order();
        changeStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // Then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, changeStatusOrder))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }

    @DisplayName("주문의 상태가 이미 완료된 경우 예외가 발생한다")
    @Test
    void changeOrderStatus_OrderStatusAlreadyCompletion_ExceptionThrown() {
        // Given
        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        final OrderTable savedOrderTable = orderTableDao.save(orderTable);

        final Menu menu = new Menu();
        menu.setName("파닭치킨");
        menu.setPrice(BigDecimal.valueOf(18000L));
        final Menu savedMenu = menuDao.save(menu);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(savedMenu.getId());
        orderLineItem.setQuantity(1L);

        final Order order = new Order();
        order.setOrderTableId(savedOrderTable.getId());
        order.setOrderLineItems(newArrayList(orderLineItem));

        final Order savedOrder = orderService.create(order);

        // When
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        final Order completedOrder = orderDao.save(savedOrder);

        final Order changeStatusOrder = new Order();
        changeStatusOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        // Then
        final Long completedOrderId = completedOrder.getId();

        assertThatThrownBy(
                () -> orderService.changeOrderStatus(completedOrderId, changeStatusOrder))
                .isInstanceOf(IllegalArgumentException.class)
        ;
    }
}
