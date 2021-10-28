package kitchenpos.application;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderItemRequest;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@DisplayName("주문 서비스 테스트")
class OrdersServiceTest extends ServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @DisplayName("주문을 생성한다. - 실패, 주문 항목이 비어있는 경우")
    @Test
    void createFailedWhenOrderLineItemsEmpty() {
        // given
        OrderRequest orderRequest = CREATE_ORDER_REQUEST(1L, emptyList());

        // when - then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(never())
                .countByIdIn(anyList());
        then(orderTableRepository).should(never())
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .findById(any());
        then(orderRepository).should(never())
                .save(any(Orders.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문 항목의 개수와 주문 항목들의 메뉴 아이디로 조회한 개수가 다른 경우")
    @Test
    void createFailedWhenSizeNotEqual() {
        // given
        OrderRequest orderRequest = CREATE_ORDER_REQUEST(1L, Arrays.asList(
                CREATE_ORDER_LINE_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_LINE_ITEM_REQUEST(2L, 1L)
        ));

        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderItemRequest::getMenuId)
                .collect(Collectors.toList());

        given(menuRepository.countByIdIn(menuIds)).willReturn(1L);

        // when - then
        // menuIds는 1이지만, orderLineItems의 사이즈는 2인 경우
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableRepository).should(never())
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .findById(any());
        then(orderRepository).should(never())
                .save(any(Orders.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문에 등록된 TableId가 존재하지 않는 경우")
    @Test
    void createFailedWhenTableIdNotFound() {
        // given

        OrderRequest orderRequest = CREATE_ORDER_REQUEST(-1L, Arrays.asList(
                CREATE_ORDER_LINE_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_LINE_ITEM_REQUEST(2L, 1L)
        ));

        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderItemRequest::getMenuId)
                .collect(Collectors.toList());

        given(menuRepository.countByIdIn(menuIds)).willReturn(2L);
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willThrow(IllegalArgumentException.class);

        // when - then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableRepository).should(times(1))
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .findById(any());
        then(orderRepository).should(never())
                .save(any(Orders.class));
    }

    @DisplayName("주문을 생성한다. - 실패, 주문에 등록된 테이블이 비어있는 경우")
    @Test
    void createFailedWhenTableIsEmpty() {
        // given
        OrderRequest orderRequest = CREATE_ORDER_REQUEST(1L, Arrays.asList(
                CREATE_ORDER_LINE_ITEM_REQUEST(1L, 1L),
                CREATE_ORDER_LINE_ITEM_REQUEST(2L, 2L)
        ));

        final List<Long> menuIds = orderRequest.getOrderLineItems().stream()
                .map(OrderItemRequest::getMenuId)
                .collect(Collectors.toList());

        given(menuRepository.countByIdIn(menuIds)).willReturn(2L);

        OrderTable orderTable = new OrderTable(1L, null, 10, true);
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(orderTable));

        // when - then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(menuRepository).should(times(1))
                .countByIdIn(menuIds);
        then(orderTableRepository).should(times(1))
                .findById(orderRequest.getOrderTableId());
        then(orderRepository).should(never())
                .findById(any());
        then(orderRepository).should(never())
                .save(any(Orders.class));
    }

    @DisplayName("주문 상태를 변경한다. - 실패, orderId에 해당하는 주문이 존재하지 않는 경우")
    @Test
    void changeOrderStatusFailedWhenOrderIdNotFound() {
        // given
        Long orderId = -1L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.MEAL.name());

        given(orderRepository.findById(orderId)).willThrow(IllegalArgumentException.class);

        // when -  then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
        then(orderRepository).should(never())
                .save(any());
    }

    @DisplayName("주문 상태를 변경한다. - 실패, orderId에 해당하는 주문이 이미 COMPLETION 상태인 경우")
    @Test
    void changeOrderStatusFailedWhenStatusIsCompletion() {
        // given
        Long orderId = 1L;
        OrderStatusRequest orderStatusRequest = CREATE_ORDER_STATUS_REQUEST(OrderStatus.COMPLETION.name());

        Orders savedOrders = new Orders(1L, null, OrderStatus.COMPLETION.name(), LocalDateTime.now());
        given(orderRepository.findById(orderId)).willReturn(Optional.of(savedOrders));

        // when -  then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(times(1))
                .findById(orderId);
        then(orderRepository).should(never())
                .save(savedOrders);
    }
}
