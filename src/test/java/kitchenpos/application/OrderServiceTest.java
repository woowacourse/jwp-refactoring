package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderRequest.OrderLineItemRequest;
import kitchenpos.dto.request.OrderStatusRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.dto.response.OrderResponse.OrderLineItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class OrderServiceTest extends ServiceTest {

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

    private OrderTable orderTable1;
    private Order order1;
    private Order order2;
    private TableGroup tableGroup;
    private MenuGroup menuGroup;
    private Menu menu;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, true);
        tableGroup = new TableGroup(1L, Arrays.asList(orderTable1, orderTable2));
        menuGroup = new MenuGroup("치킨");
        menu = new Menu(1L, "후라이드치킨", BigDecimal.valueOf(16000), menuGroup);
        order1 = new Order(orderTable1, Collections.singletonList(new OrderLineItem(menu, 2L)));
        order2 = new Order(orderTable2, Collections.singletonList(new OrderLineItem(menu, 2L)));
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable1));
        when(menuRepository.findAllById(any())).thenReturn(
            Collections.singletonList(menu)
        );
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new Order(
                1L,
                order.getOrderTable(),
                order.getOrderStatus(),
                order.getOrderLineItems()
            );
        });
        when(orderLineItemRepository.saveAll(any())).thenAnswer(
            invocation -> invocation.getArgument(0)
        );

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(
            1L,
            Collections.singletonList(orderLineItemRequest)
        );
        OrderResponse actual = orderService.create(orderRequest);

        verify(orderRepository, times(1)).save(any());
        verify(orderLineItemRepository, times(1)).saveAll(any());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getId()).isNotNull()
            .isEqualTo(actual.getOrderLineItems().get(0).getOrderId());
        assertThat(actual.getOrderLineItems()).hasSize(1);
    }

    @DisplayName("주문 항목이 0개인 주문 등록할 경우 예외 처리")
    @Test
    void createWithoutOrderLineItems() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable1));
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("등록되지 않은 메뉴로 주문 등록할 경우 예외 처리")
    @Test
    void createWithNotFoundMenu() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable1));
        when(menuRepository.findAllById(any())).thenReturn(Collections.emptyList());

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(
            1L,
            Collections.singletonList(orderLineItemRequest)
        );

        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("등록되지 않은 테이블에 주문 등록할 경우 예외 처리")
    @Test
    void createWithNotFoundOrderTable() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable1));
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(
            1L,
            Collections.singletonList(orderLineItemRequest)
        );
        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블에 주문 등록할 경우 예외 처리")
    @Test
    void createWithEmptyOrderTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(
            1L,
            Collections.singletonList(orderLineItemRequest)
        );
        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        List<Order> orders = Arrays.asList(order1, order2);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponse> actual = orderService.list();
        List<OrderResponse> expected = OrderResponse.listFrom(orders);

        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .usingElementComparatorIgnoringFields("orderLineItems")
            .hasSameElementsAs(expected);
    }

    @DisplayName("주문 상태 수정")
    @Test
    void changeOrderStatus() {
        long idToChange = 1L;
        when(orderRepository.findById(idToChange)).thenReturn(Optional.of(order1));

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        OrderResponse actual = orderService.changeOrderStatus(idToChange, orderStatusRequest);
        OrderResponse expected = new OrderResponse(
            order1.getId(),
            orderStatusRequest.getOrderStatus(),
            order1.getOrderedTime(),
            OrderLineItemResponse.listFrom(order1.getOrderLineItems())
        );

        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @DisplayName("등록되지 않은 주문 상태 수정시 예외 처리")
    @Test
    void changeOrderStatusWithNotFoundOrder() {
        long idToChange = 1L;
        when(orderRepository.findById(idToChange)).thenReturn(Optional.empty());

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(idToChange, orderStatusRequest)
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료 상태 주문의 상태 수정시 예외 처리")
    @Test
    void changeOrderStatusWith() {
        long idToChange = 1L;
        when(orderRepository.findById(idToChange)).thenReturn(Optional.of(
            new Order(
                1L,
                orderTable1,
                OrderStatus.COMPLETION.name(),
                Collections.singletonList(new OrderLineItem(menu, 2L))
            )
        ));

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(idToChange, orderStatusRequest)
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
