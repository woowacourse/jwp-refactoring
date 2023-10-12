package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
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

    @DisplayName("새 주문을 저장한다.")
    @Test
    void create_success() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final Order order = new Order();
        order.setId(1L);
        order.setOrderedTime(now);
        order.setOrderStatus(OrderStatus.COOKING.name());

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());

        order.setOrderLineItems(List.of(orderLineItem));

        final OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setId(1L);
        order.setOrderTableId(orderTable.getId());

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class)))
            .willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class)))
            .willReturn(orderLineItem);

        // when
        final Order savedOrder = orderService.create(order);

        // then
        assertThat(savedOrder).usingRecursiveComparison()
            .isEqualTo(order);
    }

    @DisplayName("주문 저장 시, 메뉴가 비어있으면 예외가 발생한다.")
    @Test
    void create_empty_fail() {
        // given
        final Order order = new Order();

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 존재하지 않는 메뉴가 있다면 예외가 발생한다.")
    @Test
    void create_notExistMenu_fail() {
        // given
        final Order order = new Order();
        order.setId(1L);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(0L);

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_notExistTable_fail() {
        // given
        final Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(1L);

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 저장 시, 주문 테이블이 비어있다면 예외가 발생한다.")
    @Test
    void create_emptyTable_fail() {
        // given
        final Order order = new Order();
        order.setId(1L);
        final OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setEmpty(true);
        order.setOrderTableId(orderTable.getId());

        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(order.getId());
        orderLineItem.setMenuId(1L);
        order.setOrderLineItems(List.of(orderLineItem));

        given(menuDao.countByIdIn(anyList()))
            .willReturn(1L);
        given(orderTableDao.findById(anyLong()))
            .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("모든 Order 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order order1 = new Order();
        order1.setId(1L);
        order1.setOrderStatus(OrderStatus.COOKING.name());

        final Order order2 = new Order();
        order2.setId(2L);
        order2.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findAll())
            .willReturn(List.of(order1, order2));

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).usingRecursiveComparison()
            .isEqualTo(List.of(order1, order2));
    }

    @DisplayName("Order 상태를 바꾼다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order prevOrder = new Order();
        prevOrder.setId(1L);
        prevOrder.setOrderStatus(OrderStatus.COOKING.name());

        final Order changeOrder = new Order();
        changeOrder.setOrderStatus(OrderStatus.MEAL.name());

        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(prevOrder));

        // when
        final Order changedOrder = orderService.changeOrderStatus(prevOrder.getId(), changeOrder);

        // then
        assertThat(changedOrder.getId()).isEqualTo(1L);
        assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("Order 상태를 바꿀 때, 존재하지 않는 주문이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_notExist_fail() {
        // given
        final Order order = new Order();
        given(orderDao.findById(anyLong()))
            .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 상태를 바꿀 때, 상태가 completion 이라면 예외가 발생한다.")
    @Test
    void changeOrderStatus_wrongStatus_fail() {
        // given
        final Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        given(orderDao.findById(anyLong()))
            .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
