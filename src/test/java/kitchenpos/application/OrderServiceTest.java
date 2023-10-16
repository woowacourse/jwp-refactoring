package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    void 주문을_생성한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setEmpty(false);

        final Order savedOrder = new Order();
        savedOrder.setId(1L);

        final OrderLineItem savedOrderLineItem = new OrderLineItem();
        savedOrderLineItem.setOrderId(1L);

        when(menuDao.countByIdIn(any()))
                .thenReturn(2L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));
        when(orderDao.save(any(Order.class)))
                .thenReturn(savedOrder);
        when(orderLineItemDao.save(any(OrderLineItem.class)))
                .thenReturn(savedOrderLineItem);

        // when
        final Order order = new Order();
        order.setOrderTableId(1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        final Order result = orderService.create(order);
        final List<OrderLineItem> orderLineItemsResult = result.getOrderLineItems();

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1),
                () -> assertThat(orderLineItemsResult).hasSize(2),
                () -> assertThat(orderLineItemsResult.get(0).getOrderId()).isEqualTo(1)
        );
    }

    @Test
    void 주문을_생성할_때_주문_항목이_없다면_실패한다() {
        // given
        final Order order = new Order();

        // when, then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_전달한_주문_항목이_DB에_존재하지_않으면_실패한다() {
        // given
        when(menuDao.countByIdIn(any()))
                .thenReturn(1L);

        // when
        final Order order = new Order();
        order.setOrderTableId(1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_전달한_주문_테이블이_DB에_존재하지_않으면_실패한다() {
        // given
        when(menuDao.countByIdIn(any()))
                .thenReturn(2L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.empty());

        // when
        final Order order = new Order();
        order.setOrderTableId(1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_생성할_때_주문_테이블이_빈_상태면_실패한다() {
        // given
        final OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);

        when(menuDao.countByIdIn(any()))
                .thenReturn(2L);
        when(orderTableDao.findById(anyLong()))
                .thenReturn(Optional.of(savedOrderTable));

        // when
        final Order order = new Order();
        order.setOrderTableId(1L);

        final OrderLineItem orderLineItem1 = new OrderLineItem();
        final OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        orderLineItem2.setMenuId(2L);
        order.setOrderLineItems(List.of(orderLineItem1, orderLineItem2));

        // then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }
}