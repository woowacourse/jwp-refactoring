package kitchenpos.application;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);

        orderTable = new OrderTable();

        order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        // when, then
        assertDoesNotThrow(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 생성 시 주문 항목들이 없다면 예외를 발생시킨다.")
    void throwExceptionWhenCreatingOrderWithEmptyOrderTable() {
        // given
        order.setOrderLineItems(Collections.emptyList());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 생성 시 주문 항목의 수와 메뉴의 수가 일치하지 않으면 예외를 발생시킨다.")
    void throwExceptionWhenDifferentSizeOfMenuAndOrderLineItems() {
        // given
        given(menuDao.countByIdIn(any())).willReturn(2L);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 생성 시 주문 테이블이 존재하지 않으면 예외를 발생시킨다.")
    void throwExceptionWhenOrderTableNull() {
        // given
        given(menuDao.countByIdIn(any())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 생성 시 주문 테이블이 비어 있으면 예외를 발생시킨다.")
    void throwExceptionWhenOrderTableEmpty() {
        // given
        orderTable.setEmpty(true);
        given(menuDao.countByIdIn(any())).willReturn((long) order.getOrderLineItems().size());
        given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.create(order));
    }

    @Test
    @DisplayName("모든 주문을 조회한다.")
    void findAllOrder() {
        // given
        order.setId(1L);
        given(orderDao.findAll()).willReturn(Collections.singletonList(order));

        // when
        List<Order> orders = orderService.list();

        // then
        assertEquals(1L, orders.get(0).getId());
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeStatusOfOrder() {
        // given
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // when
        Order order = orderService.changeOrderStatus(1L, this.order);

        // then
        assertEquals(1L, order.getId());
    }

    @Test
    @DisplayName("주문 상태 변경 시 존재하지 않는 주문이면 예외를 발생시킨다.")
    void throwExceptionWhenChangeStatusOfNullOrder() {
        // given
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        given(orderDao.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, this.order));
    }

    @Test
    @DisplayName("주문 상태 변경 시 완료 상태 주문이면 예외를 발생시킨다.")
    void throwExceptionWhenChangeStatusOfCompletedOrder() {
        // given
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());
        given(orderDao.findById(any())).willReturn(Optional.of(order));

        // when, then
        assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, this.order));
    }
}