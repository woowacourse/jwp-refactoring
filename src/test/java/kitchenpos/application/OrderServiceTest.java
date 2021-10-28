package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NotFoundException;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("Order 서비스 테스트")
public class OrderServiceTest extends ServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderItemService orderItemService;

    @DisplayName("주문을 생성한다. - 실패, 주문 테이블을 찾을 수 없다.")
    @Test
    void createFailedWhenOrderTableNotFound() {
        // given
        OrderRequest orderRequest = CREATE_ORDER_REQUEST(1L, Arrays.asList(
                CREATE_ORDER_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_ITEM_REQUEST(2L, 1L)
        ));
        given(orderTableRepository.findById(orderRequest.getOrderTableId()))
                .willThrow(NotFoundException.class);

        // when - then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotFoundException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .save(any(Orders.class));
        then(orderItemService).should(never())
                .create(anyList(), any(Orders.class));
    }

    @DisplayName("주문 상태를 변경한다. - 실패, 주문을 찾을 수 없다.")
    @Test
    void changeOrderStatusFailedNotFound() {
        // given
        Long orderId = -100L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.COOKING.name());
        given(orderRepository.findById(orderId)).willThrow(NotFoundException.class);

        // when- then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(NotFoundException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
    }

    @DisplayName("주문 상태를 변경한다. - 실패, 이미 완료된 상태의 주문")
    @Test
    void changeOrderStatusFailed() {
        // given
        Long orderId = 1L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.COOKING.name());
        Orders orders = new Orders(new OrderTable(10, false), OrderStatus.COMPLETION.name());
        given(orderRepository.findById(orderId)).willReturn(Optional.of(orders));

        // when- then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
    }
}
