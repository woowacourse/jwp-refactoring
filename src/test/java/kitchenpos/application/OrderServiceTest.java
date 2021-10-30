package kitchenpos.application;

import kitchenpos.DomainBuilder;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings
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

    @DisplayName("Order 를 생성한다")
    @Test
    void create() {
        // given
        MenuProduct menuProduct = DomainBuilder.createMenuProduct(
                1L,
                1L
        );
        Menu menu = DomainBuilder
                .createMenuWithId(
                        1L,
                        "후라이드+후라이드",
                        new BigDecimal(19000),
                        1L,
                        Collections.singletonList(menuProduct)
                );
        List<Long> menuIds = Collections.singletonList(menu.getId());
        OrderLineItem orderLineItem = DomainBuilder
                .createOrderLineItem(
                        menu.getId(),
                        1L
                );
        OrderTable orderTable = DomainBuilder
                .createOrderTable(
                        2,
                        false
                );
        Order order = DomainBuilder
                .createOrder(
                        orderTable.getId(),
                        Collections.singletonList(orderLineItem)
                );
        Order savedOrder = DomainBuilder
                .createOrder(
                        1L,
                        orderTable.getId(),
                        OrderStatus.COOKING,
                        LocalDateTime.now(),
                        Collections.singletonList(orderLineItem)
                );
        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));
        when(orderDao.save(order)).thenReturn(savedOrder);
        when(orderLineItemDao.save(orderLineItem)).thenReturn(orderLineItem);

        // when
        Order result = orderService.create(order);

        // then
        verify(menuDao, times(1)).countByIdIn(menuIds);
        verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        verify(orderDao, times(1)).save(order);
        verify(orderLineItemDao, times(1)).save(orderLineItem);
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(savedOrder);
    }
    
    @DisplayName("Order 생성 실패한다 - orderLineItems 가 비어있는 경우")
    @Test
    void createFail_whenOrderLineItemIsNotExisting() {
        // given
        Order order = mock(Order.class);
        when(order.getOrderLineItems()).thenReturn(Collections.emptyList());

        // when // then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(menuDao, never()).countByIdIn(anyList());
    }

    @DisplayName("Order 생성 실패한다 - orderLineItem 개수가 menu 수와 일치하지 않는 경우")
    @Test
    void createFail_whenOrderLineItemCountIsNotEqualToMenuCount() {
        // given
        Long menuId = 1L;
        List<Long> menuIds = Collections.singletonList(menuId);
        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        Order order = mock(Order.class);
        when(order.getOrderLineItems()).thenReturn(Collections.singletonList(orderLineItem));
        when(orderLineItem.getMenuId()).thenReturn(menuId);

        when(menuDao.countByIdIn(menuIds)).thenReturn(0L);

        // when // then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(menuDao, times(1)).countByIdIn(menuIds);
        verify(orderTableDao, never()).findById(anyLong());
    }

    @DisplayName("Order 생성 실패한다 - orderTable 이 존재하지 않는 경우")
    @Test
    void createFail_whenOrderTableIsNotExisting() {
        // given
        Long menuId = 1L;
        List<Long> menuIds = Collections.singletonList(menuId);
        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        Order order = mock(Order.class);
        when(order.getOrderLineItems()).thenReturn(Collections.singletonList(orderLineItem));
        when(orderLineItem.getMenuId()).thenReturn(menuId);

        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(menuDao, times(1)).countByIdIn(menuIds);
        verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        verify(orderDao, never()).save(order);
    }

    @DisplayName("Order 생성 실패한다 - orderTable 이 비어있는 경우")
    @Test
    void createFail_whenOrderTableIsEmpty() {
        // given
        Long menuId = 1L;
        List<Long> menuIds = Collections.singletonList(menuId);
        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        OrderTable orderTable = mock(OrderTable.class);
        Order order = mock(Order.class);
        when(order.getOrderLineItems()).thenReturn(Collections.singletonList(orderLineItem));
        when(orderLineItem.getMenuId()).thenReturn(menuId);
        when(orderTable.isEmpty()).thenReturn(true);

        when(menuDao.countByIdIn(menuIds)).thenReturn(1L);
        when(orderTableDao.findById(order.getOrderTableId())).thenReturn(Optional.of(orderTable));

        // when // then
        assertThatThrownBy(() -> orderService.create(order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(menuDao, times(1)).countByIdIn(menuIds);
        verify(orderTableDao, times(1)).findById(order.getOrderTableId());
        verify(orderDao, never()).save(order);
    }

    @DisplayName("모든 Order 를 조회한다")
    @Test
    void list() {
        // given
        OrderLineItem orderLineItem = mock(OrderLineItem.class);
        Order order = mock(Order.class);
        when(orderDao.findAll()).thenReturn(Collections.singletonList(order));
        when(orderLineItemDao.findAllByOrderId(order.getId())).thenReturn(Collections.singletonList(orderLineItem));

        // when
        List<Order> result = orderService.list();

        // then
        verify(orderDao, times(1)).findAll();
        verify(orderLineItemDao, times(1)).findAllByOrderId(order.getId());
        assertThat(result).containsExactly(order);
    }

    @DisplayName("Order 의 상태를 변경한다")
    @Test
    void changeOrderStatus() {
        // given
        Long orderId = 1L;
        LocalDateTime now = LocalDateTime.now();
        List<OrderLineItem> orderLineItems = Collections.singletonList(mock(OrderLineItem.class));
        Order savedOrder = DomainBuilder
                .createOrder(
                        orderId,
                        1L,
                        OrderStatus.COOKING,
                        now,
                        orderLineItems
                );
        Order order = DomainBuilder
                .createOrder(
                        null,
                        1L,
                        OrderStatus.MEAL,
                        now,
                        orderLineItems
                );
        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));
        when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(orderLineItems);

        // when
        Order result = orderService.changeOrderStatus(orderId, order);

        // then
        verify(orderDao, times(1)).findById(orderId);
        verify(orderDao, times(1)).save(savedOrder);
        verify(orderLineItemDao, times(1)).findAllByOrderId(orderId);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(order);
    }

    @DisplayName("Order 상태 변경 실패한다 - orderId 에 대한 order 가 존재하지 않는 경우")
    @Test
    void changeOrderStatusFail_whenOrderIsNotExisting() {
        // given
        Long orderId = 1L;
        Order order = mock(Order.class);
        when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        // when // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, order))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(orderDao, never()).save(any(Order.class));
    }

    @DisplayName("Order 상태 변경 실패한다 - order 가 이미 완료된 order 인 경우")
    @Test
    void changeOrderStatusFail_whenOrderIsAlreadyCompleted() {
        // given
        Long orderId = 1L;
        Order savedOrder = mock(Order.class);
        Order newOrder = mock(Order.class);
        when(orderDao.findById(orderId)).thenReturn(Optional.of(savedOrder));
        when(savedOrder.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

        // when // then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, newOrder))
                .isExactlyInstanceOf(IllegalArgumentException.class);
        verify(orderDao, never()).save(any(Order.class));
    }
}
