package kitchenpos.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.dtos.OrderLineItemRequest;
import kitchenpos.application.dtos.OrderRequest;
import kitchenpos.application.dtos.OrderResponse;
import kitchenpos.application.dtos.OrderResponses;
import kitchenpos.application.dtos.OrderStatusRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;
    private Order order;
    private OrderRequest orderRequest;
    private OrderStatusRequest orderStatusRequest;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .orderTableId(1L)
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();
        final OrderLineItem orderLineItem1 = OrderLineItem.builder()
                .order(order)
                .menuId(1L)
                .quantity(1L)
                .build();
        final OrderLineItem orderLineItem2 = OrderLineItem.builder()
                .order(order)
                .menuId(2L)
                .quantity(2L)
                .build();
        final List<OrderLineItem> orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
        order.updateOrderLineItems(orderLineItems);
        orderTable = OrderTable.builder()
                .id(1L)
                .empty(false)
                .build();
        final List<OrderLineItemRequest> orderLineItemRequests = orderLineItems.stream()
                .map(OrderLineItemRequest::new)
                .collect(Collectors.toList());
        orderRequest = new OrderRequest(orderTable.getId(), orderLineItemRequests);
        orderStatusRequest = new OrderStatusRequest(OrderStatus.COMPLETION.name());
    }

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void create() {
        final Order savedOrder = Order.builder()
                .of(order)
                .id(1L)
                .build();
        when(menuRepository.countByIdIn(any())).thenReturn(2L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));
        when(orderRepository.save(any())).thenReturn(savedOrder);

        final OrderResponse actual = orderService.create(orderRequest);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new OrderResponse(savedOrder));
    }

    @DisplayName("주문 항목의 목록이 비어있으면 안 된다")
    @Test
    void createExceptionEmpty() {
        final OrderRequest request = new OrderRequest(orderTable.getId(), Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 갯수와 메뉴 id의 갯수가 일치하여야 한다")
    @Test
    void createExceptionVerifySize() {
        when(menuRepository.countByIdIn(any())).thenReturn(Long.MAX_VALUE);

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재해야 한다")
    @Test
    void createExceptionTableExists() {
        when(menuRepository.countByIdIn(any())).thenReturn(2L);
        when(orderTableRepository.findById(order.getOrderTableId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있으면 안 된다")
    @Test
    void createExceptionTableEmpty() {
        final OrderTable savedOrderTable = OrderTable.builder()
                .of(orderTable)
                .empty(true)
                .build();

        when(menuRepository.countByIdIn(any())).thenReturn(2L);
        when(orderTableRepository.findById(order.getOrderTableId())).thenReturn(Optional.of(savedOrderTable));

        assertThatThrownBy(() -> orderService.create(orderRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다")
    @Test
    void list() {
        final Order savedOrder1 = Order.builder()
                .of(this.order)
                .id(1L)
                .build();
        final Order savedOrder2 = Order.builder()
                .of(this.order)
                .id(2L)
                .build();
        final List<Order> orders = Arrays.asList(savedOrder1, savedOrder2);

        when(orderRepository.findAll()).thenReturn(orders);

        final OrderResponses actual = orderService.list();
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new OrderResponses(orders));
    }

    @DisplayName("주문의 상태를 바꿀 수 있다")
    @Test
    void changeStatus() {
        final Order savedOrder = Order.builder()
                .of(this.order)
                .orderStatus(OrderStatus.MEAL.name())
                .build();

        when(orderRepository.findById(any())).thenReturn(Optional.of(savedOrder));

        final OrderResponse actual = orderService.changeOrderStatus(any(), orderStatusRequest);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(new OrderResponse(savedOrder));
    }

    @DisplayName("기존에 저장되어 있는 주문이 있어야 한다")
    @Test
    void changeStatusExceptionOrderExists() {
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.changeOrderStatus(any(), orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("기존의 주문이 `COMPLETION` 상태이면 안 된다")
    @Test
    void changeStatusExceptionStatus() {
        final Order savedOrder = Order.builder()
                .of(this.order)
                .orderStatus(OrderStatus.COMPLETION.name())
                .build();

        when(orderRepository.findById(any())).thenReturn(Optional.of(savedOrder));

        assertThatThrownBy(() -> orderService.changeOrderStatus(any(), orderStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
