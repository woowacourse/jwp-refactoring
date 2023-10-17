package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void 주문_아이템이_존재하지_않는_경우_예외가_발생한다() {
        Order order = new Order();
        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목에_메뉴_ID가_존재하지_않는경우_예외가_발생한다() {
        Order order = new Order();
        OrderLineItem orderLineItem = OrderLineItemFixtures.로제떡볶이_주문항목();
        order.setOrderLineItems(List.of(orderLineItem));

        when(menuDao.countByIdIn(List.of(1L))).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
        Order order = new Order();
        order.setOrderTableId(2L);
        OrderLineItem orderLineItem = OrderLineItemFixtures.로제떡볶이_주문항목();
        order.setOrderLineItems(List.of(orderLineItem));

        when(menuDao.countByIdIn(List.of(1L))).thenReturn(1L);

        when(orderTableDao.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성할_수_있다() {
        Order order = new Order();
        order.setOrderTableId(2L);
        OrderLineItem orderLineItem = OrderLineItemFixtures.로제떡볶이_주문항목();
        order.setOrderLineItems(List.of(orderLineItem));

        when(menuDao.countByIdIn(List.of(1L))).thenReturn(1L);

        when(orderTableDao.findById(2L)).thenReturn(Optional.of(OrderTableFixtures.주문테이블1번()));
        when(orderDao.save(order)).thenReturn(order);

        orderService.create(order);

        verify(orderDao).save(order);
        verify(orderLineItemDao).save(orderLineItem);
    }

    @Test
    void 전체_주문_조회할_수_있다() {
        Order order1 = new Order();
        order1.setId(1L);
        Order order2 = new Order();
        order2.setId(2L);
        when(orderDao.findAll()).thenReturn(List.of(order1, order2));

        orderService.list();

        verify(orderLineItemDao, times(2)).findAllByOrderId(anyLong());

    }

    @Test
    void 주문_ID가_존재하지_않는_경우_예외가_발생한다() {
        Order order = new Order();

        when(orderDao.findById(order.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(null, order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_COMPLETION_상태이면_예외가_발생한다() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(String.valueOf(OrderStatus.COMPLETION));

        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(String.valueOf(OrderStatus.COOKING));

        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(order.getId(), order);

        verify(orderDao).save(order);
        verify(orderLineItemDao).findAllByOrderId(order.getId());
    }
}
