package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.OrderStatusChangeRequest;
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
import java.util.stream.Collectors;

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
        final LocalDateTime orderedTime = LocalDateTime.now();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTable.getId(), "status", orderedTime, orderLineItems);

        final Order expected = new Order(1L, order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems());

        final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), order.getOrderStatus(), orderedTime, orderLineItemRequests);

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(orderTable));
        when(orderDao.save(any()))
                .thenReturn(expected);
        when(orderLineItemDao.save(any()))
                .thenReturn(orderLineItem);

        //when
        final Order result = orderService.create(orderCreateRequest);

        //then
        assertThat(result).isEqualTo(new Order(1L, order.getOrderStatus(), order.getOrderedTime(), order.getOrderLineItems()));
    }

    @Test
    @DisplayName("주문을 생성 시 아이템 목록이 비어있으면 예외가 발생한다")
    void testCreateWhenOrderLineItemsEmptyFailure() {
        //given
        final Order order = new Order(1L, "status", LocalDateTime.now(), Collections.emptyList());

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(1L, order.getOrderStatus(),
                LocalDateTime.now(), Collections.emptyList());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 아이템 목록과 메뉴 수가 일치하지 않으면 예외가 발생한다")
    void testCreateWhenOrderLineItemNotEqualMenuCountFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        final LocalDateTime orderedTime = LocalDateTime.now();
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final Order order = new Order(orderTable.getId(), "status", orderedTime, orderLineItems);

        final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), order.getOrderStatus(), orderedTime, orderLineItemRequests);

        when(menuDao.countByIdIn(any()))
                .thenReturn(2L);

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 주문 테이블을 찾지 못할 경우 예외가 발생한다")
    void testCreateWhenOrderTableNotFoundFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final LocalDateTime orderedTime = LocalDateTime.now();
        final Order order = new Order(orderTable.getId(), "status", orderedTime, orderLineItems);

        final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), order.getOrderStatus(), orderedTime, orderLineItemRequests);

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(any()))
                .thenThrow(new IllegalArgumentException());

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문을 생성 시 주문 테이블이 비어있을 경우 예외가 발생한다")
    void testCreateWhenOrderTableEmptyFailure() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final OrderTable orderTable = new OrderTable(1L, 1L, 1, true);
        final List<OrderLineItem> orderLineItems = List.of(orderLineItem);
        final LocalDateTime orderedTime = LocalDateTime.now();
        final Order order = new Order(orderTable.getId(), "status", orderedTime, orderLineItems);

        final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(it -> new OrderLineItemRequest(it.getMenuId(), it.getQuantity()))
                .collect(Collectors.toList());

        final OrderCreateRequest orderCreateRequest = new OrderCreateRequest(orderTable.getId(), order.getOrderStatus(), orderedTime, orderLineItemRequests);

        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);
        when(orderTableDao.findById(any()))
                .thenReturn(Optional.of(orderTable));

        //when
        //then
        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 리스트를 성공적으로 조회한다")
    void testListSuccess() {
        //given
        final Order order1 = new Order(1L, 1L, "status", LocalDateTime.now(), null);
        final Order order2 = new Order(2L, 2L, "status", LocalDateTime.now(), null);
        final Order order3 = new Order(3L, 3L, "status", LocalDateTime.now(), null);

        final OrderLineItem orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 1L);
        final OrderLineItem orderLineItem2 = new OrderLineItem(2L, 2L, 2L, 2L);
        final OrderLineItem orderLineItem3 = new OrderLineItem(3L, 3L, 3L, 3L);

        when(orderDao.findAll())
                .thenReturn(List.of(order1, order2, order3));
        when(orderLineItemDao.findAllByOrderId(any()))
                .thenReturn(List.of(orderLineItem1));
        when(orderLineItemDao.findAllByOrderId(any()))
                .thenReturn(List.of(orderLineItem2));
        when(orderLineItemDao.findAllByOrderId(any()))
                .thenReturn(List.of(orderLineItem3));

        //when
        final List<Order> result = orderService.list();

        //then
        assertThat(result).isEqualTo(List.of(order1, order2, order3));
        assertThat(result.get(0).getOrderLineItems().get(0)).isEqualTo(orderLineItem3);
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

        final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("COOKING");

        when(orderDao.findById(any()))
                .thenReturn(Optional.of(savedOrder));
        when(orderDao.save(any()))
                .thenReturn(savedOrder);
        when(orderLineItemDao.findAllByOrderId(any()))
                .thenReturn(new ArrayList<>());

        // When
        final Order result = orderService.changeOrderStatus(orderId, orderStatusChangeRequest);

        // Then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문을 찾지 못할 경우 예외가 발생한다")
    void testChangeOrderStatusWhenOrderNotFoundFailure() {
        //given
        final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("COOKING");

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderStatusChangeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상태 변경 시 주문이 이미 완료된 상태라면 예외가 발생한다")
    void testChangeOrderStatusWhenOrderStatusCompletionFailure() {
        //given
        final Order savedOrder = new Order();
        savedOrder.setOrderStatus(OrderStatus.COMPLETION.name());

        final OrderStatusChangeRequest orderStatusChangeRequest = new OrderStatusChangeRequest("COOKING");

        when(orderDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrder));

        //when
        //then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, orderStatusChangeRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
