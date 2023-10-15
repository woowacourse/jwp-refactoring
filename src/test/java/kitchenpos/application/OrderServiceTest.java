package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("주문을 성공적으로 생성한다")
    void testCreateSuccess() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        final Order order = new Order(orderTable.getId(), "status", LocalDateTime.now(), List.of(orderLineItem));

        final Order expected = new Order(1L, order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.save(order))
                .thenReturn(expected);
        when(orderLineItemDao.save(orderLineItem))
                .thenReturn(orderLineItem);

        //when
        final Order result = orderService.create(order);

        //then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("주문을 생성 시 아이템 목록이 비어있으면 예외가 발생한다")
    void testCreateWhenOrderLineItemsEmptyFailure() {
        //given
        final Order order = new Order(1L, "status", LocalDateTime.now(), Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 아이템 목록과 메뉴 수가 일치하지 않으면 예외가 발생한다")
    void testCreateWhenOrderLineItemNotEqualMenuCountFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        final Order order = new Order(orderTable.getId(), "status", LocalDateTime.now(), List.of(orderLineItem));

        when(menuDao.countByIdIn(any()))
                .thenReturn(2L);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 주문 테이블을 찾지 못할 경우 예외가 발생한다")
    void testCreateWhenOrderTableNotFoundFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        final Order order = new Order(orderTable.getId(), "status", LocalDateTime.now(), List.of(orderLineItem));

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId()))
                .thenThrow(new IllegalArgumentException());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 주문 테이블이 비어있을 경우 예외가 발생한다")
    void testCreateWhenOrderTableEmptyFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, true);
        final Order order = new Order(orderTable.getId(), "status", LocalDateTime.now(), List.of(orderLineItem));

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId()))
                .thenReturn(Optional.of(orderTable));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 리스트를 성공적으로 조회한다")
    void testListSuccess() {
        //given
        final Order order1 = new Order(1L, "status", LocalDateTime.now(), null);
        final Order order2 = new Order(2L, "status", LocalDateTime.now(), null);
        final Order order3 = new Order(3L, "status", LocalDateTime.now(), null);

        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 2L);
        final OrderLineItem orderLineItem3 = new OrderLineItem(3L, 3L, 3L, 3L);

        when(orderDao.findAll())
                .thenReturn(List.of(order1, order2, order3));
        when(orderLineItemDao.findAllByOrderId(order1.getId()))
                .thenReturn(List.of(orderLineItem1));
        when(orderLineItemDao.findAllByOrderId(order2.getId()))
                .thenReturn(List.of(orderLineItem2));
        when(orderLineItemDao.findAllByOrderId(order3.getId()))
                .thenReturn(List.of(orderLineItem3));

        //when
        final List<Order> result = orderService.list();

        //then
        order1.setOrderLineItems(List.of(orderLineItem1));
        order2.setOrderLineItems(List.of(orderLineItem2));
        order3.setOrderLineItems(List.of(orderLineItem3));
        assertThat(result).isEqualTo(List.of(order1, order2, order3));
    }

    @Test
    @DisplayName("주문 상태를 성공적으로 수정한다")
    void testChangeOrderStatusSuccess() {
        // given
        final Long orderId = 1L;
        final Order order = new Order();
        order.setOrderStatus(OrderStatus.COOKING.name());

        final Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.MEAL.name());

        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));
        when(orderDao.save(savedOrder)).thenReturn(savedOrder);
        when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(new ArrayList<>());

        // When
        final Order result = orderService.changeOrderStatus(orderId, order);

        // Then
        assertThat(OrderStatus.COOKING.name()).isEqualTo(result.getOrderStatus());
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문을 찾지 못할 경우 예외가 발생한다")
    void testChangeOrderStatusOrderNotFoundFailure() {
        //given
        final Order order = new Order(1L, "status", LocalDateTime.now(), List.of());
        when(orderDao.findById(anyLong()))
                .thenThrow(new IllegalArgumentException());

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
