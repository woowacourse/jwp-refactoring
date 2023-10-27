package kitchenpos.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.ordertable.application.OrderValidatorImpl;
import kitchenpos.ordertable.domain.repository.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.order.application.dto.OrderStatusRequest;
import kitchenpos.order.application.dto.OrderCreateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderResponse;
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
    private OrderValidatorImpl orderValidatorImpl;

    @Test
    @DisplayName("주문한다.")
    void testCreateOrder() {
        // given
        long expectedOrderId = 1L;
        OrderCreateRequest request = new OrderCreateRequest(1L, Arrays.asList(
                new OrderLineItemRequest(1L, 1L),
                new OrderLineItemRequest(2L, 2L)
        ));
        Menu menu1 = mock(Menu.class);
        Menu menu2 = mock(Menu.class);
        Order order = mock(Order.class);

        given(menuRepository.getById(1L)).willReturn(menu1);
        given(menuRepository.getById(2L)).willReturn(menu2);

        given(order.getId()).willReturn(expectedOrderId);
        given(orderRepository.save(any(Order.class))).willReturn(order);
        willDoNothing().given(orderValidatorImpl).validate(any());

        // when
        Long result = orderService.order(request);

        // then
        assertThat(result).isEqualTo(expectedOrderId);
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
