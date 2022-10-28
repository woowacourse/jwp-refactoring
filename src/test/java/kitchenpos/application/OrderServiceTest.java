package kitchenpos.application;

import static kitchenpos.Fixture.ORDER;
import static kitchenpos.Fixture.ORDER_LINE_ITEM;
import static kitchenpos.Fixture.ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.dto.request.OrderCreateRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
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
        given(menuDao.countByIdIn(anyList())).willReturn(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(ORDER_TABLE));
        given(orderDao.save(any(Order.class))).willReturn(ORDER);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(ORDER_LINE_ITEM);

        //when
        OrderCreateRequest dto = new OrderCreateRequest(1L, List.of(new OrderLineItem(1L, 1L)));
        Order savedOrder = orderService.create(dto);

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
