package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuRepository;
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
    private MenuRepository menuRepository;
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
        Order 주문1번 = OrderFixtures.주문1번();
        주문1번.setOrderLineItems(List.of(OrderLineItemFixtures.로제떡볶이_주문항목()));
        assertThatThrownBy(() -> orderService.create(주문1번))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목에_메뉴_ID가_존재하지_않는경우_예외가_발생한다() {
        Order 주문1번 = OrderFixtures.주문1번();
        OrderLineItem orderLineItem = OrderLineItemFixtures.로제떡볶이_주문항목();
        주문1번.setOrderLineItems(List.of(orderLineItem));

        when(menuRepository.countByIdIn(List.of(1L))).thenReturn(0L);

        assertThatThrownBy(() -> orderService.create(주문1번)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는_경우_예외가_발생한다() {
        Order 주문2번 = OrderFixtures.주문2번();
        OrderLineItem 로제떡볶이_주문항목 = OrderLineItemFixtures.로제떡볶이_주문항목();
        주문2번.setOrderLineItems(List.of(로제떡볶이_주문항목));

        when(menuRepository.countByIdIn(List.of(1L))).thenReturn(1L);

        when(orderTableDao.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(주문2번)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_생성할_수_있다() {
        Order 주문2번 = OrderFixtures.주문2번();
        OrderLineItem 로제떡볶이_주문항목 = OrderLineItemFixtures.로제떡볶이_주문항목();
        주문2번.setOrderLineItems(List.of(로제떡볶이_주문항목));

        when(menuRepository.countByIdIn(List.of(1L))).thenReturn(1L);

        OrderTable 주문테이블2번 = OrderTableFixtures.주문테이블2번();
        when(orderTableDao.findById(2L)).thenReturn(Optional.of(주문테이블2번));
        when(orderDao.save(any(Order.class))).thenReturn(주문2번);

        orderService.create(주문2번);

        verify(orderDao).save(any(Order.class));
        verify(orderLineItemDao).save(로제떡볶이_주문항목);
    }

    @Test
    void 전체_주문_조회할_수_있다() {
        Order 주문1번 = OrderFixtures.주문1번();
        Order 주문2번 = OrderFixtures.주문2번();
        when(orderDao.findAll()).thenReturn(List.of(주문1번, 주문2번));
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(List.of(OrderLineItemFixtures.로제떡볶이_주문항목()));

        orderService.list();

        verify(orderLineItemDao, times(2)).findAllByOrderId(anyLong());

    }

    @Test
    void 주문_ID가_존재하지_않는_경우_예외가_발생한다() {
        Order order = new Order(null, null);

        when(orderDao.findById(order.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(null, order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_상태가_COMPLETION_상태이면_예외가_발생한다() {
        Order order = OrderFixtures.주문1번();
        order.setOrderStatus(OrderStatus.COMPLETION);

        when(orderDao.findById(order.getId())).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), order)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_변경할_수_있다() {
        Order 주문1번 = OrderFixtures.주문1번();
        주문1번.setOrderStatus(OrderStatus.COOKING);

        when(orderDao.findById(주문1번.getId())).thenReturn(Optional.of(주문1번));
        when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(List.of(OrderLineItemFixtures.로제떡볶이_주문항목()));
        orderService.changeOrderStatus(주문1번.getId(), 주문1번);

        verify(orderDao).save(주문1번);
        verify(orderLineItemDao).findAllByOrderId(주문1번.getId());
    }
}
