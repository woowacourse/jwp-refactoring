package kitchenpos.application;

import static kitchenpos.Fixture.ORDER;
import static org.assertj.core.api.Assertions.assertThat;
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

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("주문을 한다.")
    @Test
    void create() {
        //given
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem(1L, 1L);
        OrderTable orderTable = new OrderTable(1L, 1L, 1, false);
        order.setOrderLineItems(List.of(orderLineItem));
        order.setOrderTableId(orderTable.getId());
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.save(order)).willReturn(ORDER);
        given(orderLineItemDao.save(orderLineItem)).willReturn(orderLineItem);

        //when
        Order savedOrder = orderService.create(order);

        //then
        assertThat(savedOrder.getOrderStatus()).isNotNull();
        assertThat(savedOrder.getOrderedTime()).isNotNull();
        assertThat(savedOrder.getOrderLineItems()).isNotEmpty();
    }

    @DisplayName("주문 내역을 조회한다.")
    @Test
    void list() {
        // given
        given(orderDao.findAll()).willReturn(List.of(ORDER));

        //then
        List<Order> orders = orderService.list();

        //given
        assertThat(orders).hasSize(1);
    }
}
