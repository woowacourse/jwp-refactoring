package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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
    @DisplayName("주문 생성 테스트")
    public void createOrderTest() {
        //given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderLineItems(List.of(new OrderLineItem()));

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(new OrderLineItem());

        //when
        Order createdOrder = orderService.create(order);

        //then
        assertThat(createdOrder.getOrderTableId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 목록 조회 테스트")
    public void listOrdersTest() {
        //given
        final Order order = new Order();
        order.setId(1L);
        given(orderDao.findAll()).willReturn(List.of(order));
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(List.of(new OrderLineItem()));

        //when
        final List<Order> list = orderService.list();

        //then
        assertThat(list).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 변경 테스트")
    public void changeOrderStatusTest() {
        //given
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus("COOKING");

        given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
        given(orderDao.save(any(Order.class))).willReturn(order);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(List.of(new OrderLineItem()));

        //when
        Order updatedOrder = orderService.changeOrderStatus(1L, order);

        //then
        assertThat(updatedOrder.getOrderStatus()).isEqualTo("COOKING");

    }
}
