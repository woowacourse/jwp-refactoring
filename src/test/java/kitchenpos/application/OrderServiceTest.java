package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.exception.KitchenposException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.exception.KitchenposException.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class OrderServiceTest extends ServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        Order savedOrder = new Order();
        savedOrder.setId(1L);
        savedOrder.setOrderStatus(OrderStatus.COOKING.name());
        savedOrder.setOrderedTime(LocalDateTime.now());
        savedOrder.setOrderTableId(1L);
        savedOrder.setOrderLineItems(orderLineItems);

        when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.save(any(Order.class)))
                .thenReturn(savedOrder);
        when(orderLineItemDao.save(any(OrderLineItem.class)))
                .thenReturn(orderLineItem);

        Order actual = orderService.create(order);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }

    @Test
    @DisplayName("주문에 주문항목이 존재하지 않으면 에러가 발생한다.")
    void createExceptionEmptyOrderLineItems() {
        order.setOrderLineItems(Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(EMPTY_ORDER_LINE_ITEMS);
    }

    @Test
    @DisplayName("주문항목과 메뉴의 갯수가 일치하지 않으면 에러가 발생한다.")
    void createExceptionCountOrderItems() {
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(2L);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_ITEM_SIZE);
    }

    @Test
    @DisplayName("주문테이블이 존재하지 않으면 에러가 발생한다.")
    void createExceptionExistOrderTable() {
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_ORDER_TABLE_ID);
    }

    @Test
    @DisplayName("주문테이블이 비어있으면 에러가 발생한다.")
    void createExceptionEmptyOrderTable() {
        when(menuDao.countByIdIn(anyList()))
                .thenReturn(1L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(orderTable));
        orderTable.setEmpty(true);

        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(EMPTY_ORDER_TABLE);
    }

    @Test
    @DisplayName("모든 주문을 조회한다.")
    void list() {
        Order order1 = new Order();
        order1.setId(2L);
        order1.setOrderStatus(OrderStatus.COOKING.name());
        order1.setOrderedTime(LocalDateTime.now());
        order1.setOrderTableId(1L);
        order1.setOrderLineItems(orderLineItems);

        List<Order> orders = new ArrayList<>();
        orders.add(order);
        orders.add(order1);

        when(orderDao.findAll())
                .thenReturn(orders);
        when(orderLineItemDao.findAllByOrderId(anyLong()))
                .thenReturn(orderLineItems);

        List<Order> actual = orderService.list();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(orders);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        Order completeOrder = new Order();
        completeOrder.setId(order.getId());
        completeOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        completeOrder.setOrderedTime(order.getOrderedTime());
        completeOrder.setOrderTableId(order.getOrderTableId());
        completeOrder.setOrderLineItems(order.getOrderLineItems());

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.of(order));
        when(orderDao.save(any(Order.class)))
                .thenReturn(completeOrder);
        when(orderLineItemDao.findAllByOrderId(anyLong()))
                .thenReturn(orderLineItems);

        Order actual = orderService.changeOrderStatus(1L, completeOrder);
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(completeOrder);
    }

    @Test
    @DisplayName("주문이 올바르지 않으면 에러가 발생한다.")
    void changeOrderStatusExceptionIllegalOrder() {
        Order completeOrder = new Order();
        completeOrder.setId(order.getId());
        completeOrder.setOrderStatus(OrderStatus.COMPLETION.name());
        completeOrder.setOrderedTime(order.getOrderedTime());
        completeOrder.setOrderTableId(order.getOrderTableId());
        completeOrder.setOrderLineItems(order.getOrderLineItems());

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.empty());
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, completeOrder))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(ILLEGAL_ORDER_ID);
    }

    @Test
    @DisplayName("변경하고자 하는 상태가 완료되면 에러가 발생한다.")
    void changeOrderStatusExceptionSameStatus() {
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.of(order));
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(KitchenposException.class)
                .hasMessage(SAME_ORDER_STATUS);
    }
}
