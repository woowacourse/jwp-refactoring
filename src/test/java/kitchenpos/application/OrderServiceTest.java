package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1L);
        final OrderLineItem savedOrderLineItem1 = new OrderLineItem();
        savedOrderLineItem1.setSeq(1L);
        savedOrderLineItem1.setOrderId(1L);
        savedOrderLineItem1.setMenuId(1L);
        savedOrderLineItem1.setQuantity(1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(2L);
        final OrderLineItem savedOrderLineItem2 = new OrderLineItem();
        savedOrderLineItem2.setSeq(2L);
        savedOrderLineItem2.setOrderId(2L);
        savedOrderLineItem2.setMenuId(2L);
        savedOrderLineItem2.setQuantity(2L);
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        final List<Long> menuIds = Arrays.asList(1L, 2L);
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(false);
        final Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderTableId(1L);
        savedOrder.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

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
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 갯수와 메뉴 id의 갯수가 일치하여야 한다")
    @Test
    void createExceptionVerifySize() {
        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(2L);
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        final List<Long> menuIds = Arrays.asList(1L, 2L);

        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다")
    @Test
    void createExceptionTableExists() {
        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(2L);
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        final List<Long> menuIds = Arrays.asList(1L, 2L);

        when(menuDao.countByIdIn(menuIds)).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionTableEmpty() {
        final OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setOrderId(1L);
        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem2.setOrderId(2L);
        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(2L);
        final Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        final List<Long> menuIds = Arrays.asList(1L, 2L);
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);

        when(menuDao.countByIdIn(menuIds)).thenReturn(2L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderTableId(1L);
        final Order order2 = new Order();
        order2.setId(1L);
        order2.setOrderTableId(1L);
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
        final Long orderId = 1L;
        final Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        final Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderTableId(1L);
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));
        when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(Collections.emptyList());

        final Order actual = orderService.changeOrderStatus(orderId, order);
        assertThat(actual).isEqualTo(savedOrder);
    }

    @DisplayName("기존에 저장되어 있는 주문이 있어야 한다")
    @Test
    void changeStatusExceptionOrderExists() {
        final Long orderId = 1L;
        final Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기존의 주문이 `COMPLETION` 상태이면 안 된다")
    @Test
    void changeStatusExceptionStatus() {
        final Long orderId = 1L;
        final Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        final Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderTableId(1L);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
