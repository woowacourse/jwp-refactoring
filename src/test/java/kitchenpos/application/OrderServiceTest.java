package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2L);

        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        OrderTable table = new OrderTable();

        table.setId(1L);
        table.setEmpty(false);
        table.setNumberOfGuests(4);

        Order order = new Order();
        order.setOrderTableId(table.getId());
        order.setOrderLineItems(orderLineItems);

        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderTableId(table.getId());
        savedOrder.setOrderLineItems(orderLineItems);
        savedOrder.setOrderedTime(LocalDateTime.now());

        OrderLineItem savedOrderLineItem1 = new OrderLineItem();
        OrderLineItem savedOrderLineItem2 = new OrderLineItem();

        savedOrderLineItem1.setSeq(1L);
        savedOrderLineItem1.setOrderId(savedOrder.getId());
        savedOrderLineItem1.setMenuId(1L);
        savedOrderLineItem1.setQuantity(2L);

        savedOrderLineItem2.setSeq(2L);
        savedOrderLineItem2.setOrderId(savedOrder.getId());
        savedOrderLineItem2.setMenuId(2L);
        savedOrderLineItem2.setQuantity(1L);

        given(menuDao.countByIdIn(anyList())).willReturn((long)orderLineItems.size());
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(table));
        given(orderDao.save(any(Order.class))).willReturn(savedOrder);
        given(orderLineItemDao.save(orderLineItem1)).willReturn(savedOrderLineItem1);
        given(orderLineItemDao.save(orderLineItem2)).willReturn(savedOrderLineItem2);

        Order actual = orderService.create(order);

        assertAll(
            () -> assertThat(actual).extracting(Order::getId).isEqualTo(savedOrder.getId()),
            () -> assertThat(actual.getOrderLineItems()).extracting(OrderLineItem::getOrderId)
                .containsOnly(savedOrder.getId()),
            () -> assertThat(actual).extracting(Order::getOrderStatus).isEqualTo(OrderStatus.COOKING.name()),
            () -> assertThat(actual).extracting(Order::getOrderedTime).isNotNull()
        );
    }

    @DisplayName("주문을 추가할 시 주문 상품이 null이면 예외 처리한다.")
    @Test
    void createWithNullOrderProducts() {
        Order order = new Order();
        order.setOrderLineItems(null);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 주문 상품이 비어있으면 예외 처리한다.")
    @Test
    void createWithEmptyOrderProducts() {
        Order order = new Order();
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 존재하지 않는 MenuId일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingMenu() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2L);

        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        Order order = new Order();
        order.setOrderLineItems(orderLineItems);

        List<Long> menuIds = orderLineItems.stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn((long)(orderLineItems.size() - 1));

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 존재하지 않는 테이블일 경우 예외 처리한다.")
    @Test
    void createWithNotExistingTable() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2L);

        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(orderLineItems);

        List<Long> menuIds = orderLineItems.stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn((long)(orderLineItems.size()));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 추가할 시 테이블이 비어있다면 예외 처리한다.")
    @Test
    void createWithEmptyTable() {
        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        orderLineItem1.setMenuId(1L);
        orderLineItem1.setQuantity(2L);

        orderLineItem2.setMenuId(2L);
        orderLineItem2.setQuantity(1L);

        List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);

        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);

        Order order = new Order();
        order.setOrderTableId(orderTable.getId());
        order.setOrderLineItems(orderLineItems);

        List<Long> menuIds = orderLineItems.stream().map(OrderLineItem::getMenuId).collect(Collectors.toList());

        given(menuDao.countByIdIn(menuIds)).willReturn((long)(orderLineItems.size()));
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 전체 목록을 조회한다.")
    @Test
    void list() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);

        OrderLineItem orderLineItem1 = new OrderLineItem();
        OrderLineItem orderLineItem2 = new OrderLineItem();

        given(orderLineItemDao.findAllByOrderId(order1.getId())).willReturn(Collections.singletonList(orderLineItem1));
        given(orderLineItemDao.findAllByOrderId(order2.getId())).willReturn(Collections.singletonList(orderLineItem2));
        given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));

        List<Order> actual = orderService.list();

        assertAll(
            () -> assertThat(actual).element(0).extracting(Order::getOrderLineItems, LIST).contains(orderLineItem1),
            () -> assertThat(actual).element(1).extracting(Order::getOrderLineItems, LIST).contains(orderLineItem2)
        );
    }

    @DisplayName("주문 상태를 수정한다.")
    @Test
    void changeOrderStatus() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());

        Order updateInfo = new Order();
        updateInfo.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(savedOrder.getId())).willReturn(Optional.of(savedOrder));

        Order actual = orderService.changeOrderStatus(savedOrder.getId(), updateInfo);

        assertAll(
            () -> verify(orderDao).save(savedOrder),
            () -> assertThat(actual.getOrderStatus()).isEqualTo(updateInfo.getOrderStatus())
        );
    }

    @DisplayName("주문 상태를 수정할 시 해당되는 orderId가 없으면 예외 처리한다.")
    @Test
    void changeOrderStatusWithNotExistingOrderId() {
        given(orderDao.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태를 수정할 시 현재 주문이 Completion 상태이면 예외 처리한다.")
    @Test
    void changeOrderStatusWithCompletionStatus() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        Order updateInfo = new Order();
        updateInfo.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(savedOrder.getId())).willReturn(Optional.of(savedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(savedOrder.getId(), updateInfo))
            .isInstanceOf(IllegalArgumentException.class);
    }
}