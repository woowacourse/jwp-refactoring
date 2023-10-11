package kitchenpos.application;

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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;

    @Test
    @DisplayName("주문 생성 성공")
    void create() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of(orderLineItem));

        given(menuDao.countByIdIn(any())).willReturn(1L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(new OrderTable(1L, null, 2, false)));
        given(orderDao.save(any())).willReturn(order);
        given(orderLineItemDao.save(any())).willReturn(orderLineItem);

        //when
        final Order result = orderService.create(order);

        //then
        assertThat(result).isNotNull();
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("모든 주문 조회")
    void list() {
        //given
        final OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1);
        final Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), List.of(orderLineItem));

        given(orderDao.findAll()).willReturn(List.of(order));
        given(orderLineItemDao.findAllByOrderId(1L)).willReturn(List.of(orderLineItem));

        //when
        final List<Order> result = orderService.list();

        //then
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("주문 상태 변경")
    void changeOrderStatus() {
        //given
        final Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), null);
        given(orderDao.findById(1L)).willReturn(Optional.of(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null)));
        given(orderDao.save(any())).willReturn(order);

        //when
        final Order result = orderService.changeOrderStatus(1L, order);

        //then
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

}