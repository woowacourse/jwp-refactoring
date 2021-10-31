package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Optional;

import static kitchenpos.application.ServiceTest.DomainFactory.CREATE_ORDER;
import static kitchenpos.application.ServiceTest.DomainFactory.CREATE_ORDER_TABLE;
import static kitchenpos.application.ServiceTest.RequestFactory.CREATE_ORDER_ITEM_REQUEST;
import static kitchenpos.application.ServiceTest.RequestFactory.CREATE_ORDER_REQUEST;
import static kitchenpos.application.ServiceTest.RequestFactory.CREATE_ORDER_STATUS_REQUEST;
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
    private OrderLineItemService orderLineItemService;

    @DisplayName("주문을 생성한다. - 실패, 주문 테이블을 찾을 수 없다.")
    @Test
    void createFailedWhenOrderTableNotFound() {
        // given
        OrderRequest orderRequest = CREATE_ORDER_REQUEST(1L, Arrays.asList(
                CREATE_ORDER_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_ITEM_REQUEST(2L, 1L)
        ));
        given(orderTableRepository.findById(orderRequest.getOrderTableId()))
                .willThrow(NonExistentException.class);

        // when - then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NonExistentException.class);
        then(orderTableRepository).should(times(1))
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .save(any(Order.class));
        then(orderLineItemService).should(never())
                .createOrderLineItem(anyList(), any(Order.class));
    }

    @DisplayName("주문 상태를 변경한다. - 실패, 주문을 찾을 수 없다.")
    @Test
    void changeOrderStatusFailedNotFound() {
        // given
        Long orderId = -100L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.COOKING.name());
        given(orderRepository.findById(orderId)).willThrow(NonExistentException.class);

        // when- then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(NonExistentException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
    }

    @DisplayName("주문 상태를 변경한다. - 실패, 이미 완료된 상태의 주문")
    @Test
    void changeOrderStatusFailed() {
        // given
        Long orderId = 1L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.COOKING.name());
        Order order = CREATE_ORDER(
                null,
                CREATE_ORDER_TABLE(null, null, 10, false),
                OrderStatus.COMPLETION.name()
        );
        given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

        // when- then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
    }
}
