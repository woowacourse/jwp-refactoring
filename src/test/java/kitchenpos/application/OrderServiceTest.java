package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

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
    @DisplayName("생성")
    void create() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(3L);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        Order saved = new Order();
        saved.setId(1L);
        saved.setOrderTableId(3L);
        saved.setOrderStatus(OrderStatus.COOKING.name());

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(1L);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(saved);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(orderLineItem);

        Order result = orderService.create(order);

        assertThat(result).isNotNull();
        assertAll(
            () -> assertThat(result.getId()).isEqualTo(1L),
            () -> assertThat(result.getOrderTableId()).isEqualTo(3L),
            () -> assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
        );
    }

    @Test
    @DisplayName("수량이 있는 메뉴가 null이거나 존재하지 않는 경우")
    void orderLineItemDoesNotExist() {
        Order order = new Order();
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);

        order.setOrderLineItems(Collections.emptyList());
        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("수량이 있는 메뉴와 등록된 메뉴에 등록된 것이 다른 경우")
    void differentCountOfOrderLineItemAndMenu() {
        Order order = new Order();
        order.setId(1L);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(0L);

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 경우")
    void emptyOrderTable() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderTableId(3L);
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(2L);
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(menuDao.countByIdIn(Collections.singletonList(2L))).willReturn(1L);
        given(orderTableDao.findById(3L)).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
