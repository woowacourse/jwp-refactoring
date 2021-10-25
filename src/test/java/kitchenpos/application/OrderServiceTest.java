package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    private OrderLineItem orderLineItem1;
    private OrderLineItem orderLineItem2;
    private Order order;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderLineItem1 = OrderLineItem.builder()
                .orderId(1L)
                .menuId(1L)
                .quantity(1L)
                .build();
        orderLineItem2 = OrderLineItem.builder()
                .orderId(2L)
                .menuId(2L)
                .quantity(2L)
                .build();
        order = Order.builder()
                .orderTableId(1L)
                .orderStatus(OrderStatus.COMPLETION.name())
                .orderLineItems(Arrays.asList(orderLineItem1, orderLineItem2))
                .build();
        orderTable = OrderTable.builder()
                .id(1L)
                .empty(false)
                .build();
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
        final OrderLineItem savedOrderLineItem1 = OrderLineItem.builder()
                .of(orderLineItem1)
                .seq(1L)
                .build();
        final OrderLineItem savedOrderLineItem2 = OrderLineItem.builder()
                .of(orderLineItem2)
                .seq(2L)
                .build();
        final List<Long> menuIds = Arrays.asList(1L, 2L);
        final Order savedOrder = Order.builder()
                .of(order)
                .id(1L)
                .build();
        when(menuDao.countByIdIn(menuIds)).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(savedOrder);
        when(orderLineItemDao.save(orderLineItem1)).thenReturn(savedOrderLineItem1);
        when(orderLineItemDao.save(orderLineItem2)).thenReturn(savedOrderLineItem2);

        final Order actual = orderService.create(order);

        assertThat(actual).isEqualTo(savedOrder);
    }

    @DisplayName("주문 항목의 목록이 비어있으면 안 된다")
    @Test
    void createExceptionEmpty() {
        final Order order = Order.builder()
                .of(this.order)
                .orderLineItems(Collections.emptyList())
                .build();

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 갯수와 메뉴 id의 갯수가 일치하여야 한다")
    @Test
    void createExceptionVerifySize() {
        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다")
    @Test
    void createExceptionTableExists() {
        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        when(menuDao.countByIdIn(menuIds)).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionTableEmpty() {
        final List<Long> menuIds = order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
        final OrderTable savedOrderTable = OrderTable.builder()
                .of(orderTable)
                .empty(true)
                .build();

        when(menuDao.countByIdIn(menuIds)).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Order order1 = Order.builder()
                .of(this.order)
                .id(1L)
                .build();
        final Order order2 = Order.builder()
                .of(this.order)
                .id(2L)
                .build();
        final List<Order> orders = Arrays.asList(order1, order2);

        when(orderDao.findAll()).thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(order1.getId())).thenReturn(Collections.emptyList());
        when(orderLineItemDao.findAllByOrderId(order2.getId())).thenReturn(Collections.emptyList());

        final List<Order> actual = orderService.list();
        assertThat(actual).isEqualTo(orders);
    }

    @DisplayName("주문의 상태를 바꿀 수 있다")
    @Test
    void changeStatus() {
        final Order savedOrder = Order.builder()
                .of(this.order)
                .orderStatus(OrderStatus.MEAL.name())
                .build();

        when(orderDao.findById(any())).thenReturn(Optional.of(savedOrder));
        when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Collections.emptyList());

        final Order actual = orderService.changeOrderStatus(any(), order);
        assertThat(actual).isEqualTo(savedOrder);
    }

    @DisplayName("기존에 저장되어 있는 주문이 있어야 한다")
    @Test
    void changeStatusExceptionOrderExists() {
        when(orderDao.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(any(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기존의 주문이 `COMPLETION` 상태이면 안 된다")
    @Test
    void changeStatusExceptionStatus() {
        final Order savedOrder = Order.builder()
                .of(this.order)
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();

        when(orderDao.findById(any())).thenReturn(Optional.of(savedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(any(), order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
