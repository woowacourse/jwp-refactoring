package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Assertions;
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

    @Test
    void order_line_itmes가_비어있으면_예외를_반환한다() {
        // given
        Order order = new Order();

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_line_itmes_크기와_메뉴_ID의_개수가_맞지_않으면_예외를_반환한다() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));

        when(menuDao.countByIdIn(any())).thenReturn(3L);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_table_id에_맞는_order_table이_없으면_예외를_반환한다() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderTableId(101L);

        when(menuDao.countByIdIn(any())).thenReturn(2L);
        when(orderTableDao.findById(101L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_table이_비어있으면_예외를_반환한다() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderTableId(101L);

        when(menuDao.countByIdIn(any())).thenReturn(2L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        when(orderTableDao.findById(101L)).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order를_생성할_수_있다() {
        // given
        Order order = new Order();
        OrderLineItem orderLineItem1 = new OrderLineItem();
        orderLineItem1.setMenuId(1L);
        OrderLineItem orderLineItem2 = new OrderLineItem();
        orderLineItem1.setMenuId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem1, orderLineItem2));
        order.setOrderTableId(101L);

        when(menuDao.countByIdIn(any())).thenReturn(2L);

        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        when(orderTableDao.findById(101L)).thenReturn(Optional.of(orderTable));

        when(orderDao.save(any(Order.class))).thenReturn(order);

        // when
        Order savedOrder = orderService.create(order);

        // then
        Assertions.assertAll(
                () -> assertThat(savedOrder.getOrderStatus()).isNotNull(),
                () -> assertThat(savedOrder.getOrderedTime()).isNotNull()
        );
    }


    @Test
    void order_목록을_조회할_수_있다() {
        // given
        Order order1 = new Order();
        Order order2 = new Order();
        when(orderDao.findAll()).thenReturn(Arrays.asList(order1, order2));

        // when
        List<Order> orders = orderService.list();

        // then
        assertThat(orders).hasSize(2);
    }

    @Test
    void order_status_변경_시_일치하는_order_id가_없을_경우_예외를_반환한다() {
        // given
        Order order = new Order();
        when(orderDao.findById(101L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(101L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_status_변경_시_이미_완료상태이면_예외를_반환한다() {
        // given
        Order foundOrder = new Order();
        foundOrder.setOrderStatus(COMPLETION.name());
        when(orderDao.findById(1L)).thenReturn(Optional.of(foundOrder));
        Order order = new Order();

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void aorder_status를_변경할_수_있다() {
        // given
        Order foundOrder = new Order();
        foundOrder.setOrderStatus(COOKING.name());
        when(orderDao.findById(1L)).thenReturn(Optional.of(foundOrder));
        Order order = new Order();
        order.setOrderStatus(COMPLETION.name());

        // when
        Order updatedOrder = orderService.changeOrderStatus(1L, order);

        // then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(COMPLETION.name());
    }
}
