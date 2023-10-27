package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderStatusChangeRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.exception.OrderException.CompletionOrderException;
import kitchenpos.order.domain.exception.OrderException.EmptyOrderLineItemsException;
import kitchenpos.order.domain.exception.OrderException.EmptyOrderTableException;
import kitchenpos.order.domain.exception.OrderException.NotExistsMenuException;
import kitchenpos.order.domain.exception.OrderException.NotExistsOrderException;
import kitchenpos.order.domain.exception.OrderTableException.NotExistsOrderTableException;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private final OrderTable orderTable = new OrderTable(10);
    private final Order order = Order.of(orderTable);
    @InjectMocks
    private OrderService orderService;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void init() {
        orderTable.changeEmpty(false);
    }

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create_success() {
        when(menuRepository.countByIdIn(List.of(1L, 1L))).thenReturn(2L);
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);
        when(orderRepository.save(any())).thenReturn(order);

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        orderService.create(orderRequest);

        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
            verify(orderLineItemRepository, times(1)).saveAll(any());
            verify(orderRepository, times(1)).save(any());
        });
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목의 메뉴가 db에 존재하지 않으면 예외가 발생한다.")
    void create_fail_orderLineItems_no_menu() {
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);
        when(menuRepository.countByIdIn(List.of(1L, 1L))).thenReturn(1L);

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotExistsMenuException.class);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 항목의 메뉴가 하나도 없으면 예외가 발생한다.")
    void create_fail_orderLineItems_empty() {
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블 번호가 db에 존재하지 않으면 예외가 발생한다.")
    void create_fail_no_orderTable() {
        when(orderTableRepository.getById(1L)).thenThrow(new NotExistsOrderTableException());

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NotExistsOrderTableException.class);
    }

    @Test
    @DisplayName("주문을 생성할 때 주문 테이블이 빈 테이블이면 예외가 발생한다.")
    void create_fail_empty_orderTable() {
        orderTable.changeEmpty(true);
        when(menuRepository.countByIdIn(List.of(1L, 1L))).thenReturn(2L);
        when(orderTableRepository.getById(1L)).thenReturn(orderTable);

        List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        orderLineItemRequests.add(new OrderLineItemRequest(1L, 10));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderTableException.class);
    }

    @Test
    @DisplayName("주문 목록을 조회할 수 있다. - 0개의 주문 목록")
    void list_success1() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        orderService.list();

        assertAll(
                () -> verify(orderRepository, times(1)).findAll()
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 4})
    @DisplayName("주문 목록을 조회할 수 있다. - n개의 주문 목록")
    void list_success2(int n) {
        when(orderRepository.findAll()).thenReturn(getOrdersByNTimes(n));
        when(orderTableRepository.getById(any())).thenReturn(new OrderTable(10));
        when(orderLineItemRepository.findAllByOrder(any())).thenReturn(List.of(OrderLineItem.of(order, 1L, 10)));

        orderService.list();

        assertAll(
                () -> verify(orderRepository, times(1)).findAll()
        );
    }

    @Test
    @DisplayName("현재 주문 상태를 변경할 수 있다.")
    void changeOrderStatus_success() {
        order.changeOrderStatus(OrderStatus.COOKING);
        OrderStatus newOrderStatus = OrderStatus.MEAL;

        when(orderRepository.getById(1L)).thenReturn(order);
        when(orderTableRepository.getById(any())).thenReturn(new OrderTable(10));
        when(orderLineItemRepository.findAllByOrder(order)).thenReturn(List.of(OrderLineItem.of(order, 1L, 10)));

        OrderResponse savedOrder = orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(newOrderStatus));

        assertThat(savedOrder.getOrderStatus()).isEqualTo(newOrderStatus);
    }

    @Test
    @DisplayName("주문이 db에 저장되어있지 않으면 예외가 발생한다.")
    void changeOrderStatus_fail_no_order() {
        when(orderRepository.getById(1L)).thenThrow(NotExistsOrderException.class);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.MEAL)))
                .isInstanceOf(NotExistsOrderException.class);
    }

    @Test
    @DisplayName("주문이 현재 주문 상태가 COMPLETION이면 예외가 발생한다.")
    void changeOrderStatus_fail_completion() {
        order.changeOrderStatus(OrderStatus.COMPLETION);
        when(orderRepository.getById(1L)).thenReturn(order);

        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new OrderStatusChangeRequest(OrderStatus.MEAL)))
                .isInstanceOf(CompletionOrderException.class);
    }

    private List<Order> getOrdersByNTimes(int n) {
        List<Order> orders = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            orders.add(order);
        }
        return orders;
    }
}
