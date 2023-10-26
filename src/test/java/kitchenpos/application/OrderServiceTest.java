package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.domain.Menu;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문한다.")
    void testCreateOrder() {
        // given
        long expectedOrderId = 1L;
        OrderCreateRequest request = new OrderCreateRequest(1L, Arrays.asList(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 2L)
        ));
        OrderTable orderTable = mock(OrderTable.class);
        Menu menu1 = mock(Menu.class);
        Menu menu2 = mock(Menu.class);
        Order order = mock(Order.class);

        given(menu1.getId()).willReturn(1L);
        given(menu2.getId()).willReturn(2L);
        given(orderTableRepository.getById(anyLong())).willReturn(orderTable);
        given(menuRepository.findByIdIn(anyList())).willReturn(List.of(menu1, menu2));
        given(order.getId()).willReturn(expectedOrderId);
        given(orderRepository.save(any(Order.class))).willReturn(order);

        // when
        Long result = orderService.order(request);

        // then
        assertThat(result).isEqualTo(expectedOrderId);
        verify(orderTableRepository).getById(anyLong());
        verify(menuRepository).findByIdIn(anyList());
        verify(orderRepository).save(any(Order.class));
    }


    @Test
    @DisplayName("주문를 조회한다.")
    void testFindAllOrders() {
        // given
        Order order1 = new Order();
        Order order2 = new Order();
        given(orderRepository.findAll()).willReturn(Arrays.asList(order1, order2));

        // when
        List<OrderResponse> results = orderService.findAll();

        // then
        assertEquals(2, results.size());
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void testChangeOrderStatus() {
        // given
        Long orderId = 1L;
        final OrderStatusRequest orderStatusRequest = new OrderStatusRequest(COOKING);
        Order savedOrder = mock(Order.class);

        given(orderRepository.getById(anyLong())).willReturn(savedOrder);

        // when
        OrderResponse result = orderService.changeOrderStatus(orderId, orderStatusRequest);

        // then
        assertNotNull(result);
        verify(savedOrder).updateOrderStatus(orderStatusRequest.getOrderStatus());
    }
}
