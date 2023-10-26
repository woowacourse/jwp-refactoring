package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.OrderStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderResponse;
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
    void testCreateOrder() {
        // given
        OrderCreateRequest request = new OrderCreateRequest(1L, Arrays.asList(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 2L)
        ));
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableRepository.getById(anyLong())).willReturn(orderTable);
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderRepository.save(any(Order.class))).willReturn(new Order());

        // when
        Long result = orderService.order(request);

        // then
        verify(menuRepository).countByIdIn(anyList());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
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
