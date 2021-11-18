package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Name;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
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

    private OrderTable orderTable;
    private Order order1;
    private OrderLineItem orderLineItemOfOrder1;
    private Order order2;
    private OrderLineItem orderLineItemOfOrder2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, true);
        new TableGroup(1L, Arrays.asList(orderTable, orderTable2));
        MenuGroup menuGroup = new MenuGroup("치킨");
        menu = new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
            menuGroup,
            new MenuProducts(Collections.singletonList(
                new MenuProduct(new Product("후라이드치킨", BigDecimal.valueOf(16000)), 2L)
            ))
        );
        order1 = new Order(1L, orderTable, OrderStatus.COOKING);
        orderLineItemOfOrder1 = new OrderLineItem(order1, menu, 2L);
        order2 = new Order(2L, orderTable2, OrderStatus.COOKING);
        orderLineItemOfOrder2 = new OrderLineItem(order2, menu, 2L);
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        when(menuRepository.findAllById(any())).thenReturn(
            Collections.singletonList(menu)
        );
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new Order(
                1L,
                order.getOrderTable(),
                order.getOrderStatus()
            );
        });
        when(orderLineItemRepository.saveAll(any())).thenAnswer(invocation -> {
            List<OrderLineItem> orderLineItems = invocation.getArgument(0);
            List<OrderLineItem> result = new ArrayList<>();
            for (int seq = 1; seq <= orderLineItems.size(); seq++) {
                OrderLineItem orderLineItem = orderLineItems.get(seq - 1);
                result.add(
                    new OrderLineItem((long) seq, orderLineItem.getOrder(), orderLineItem.getMenu(),
                        orderLineItem.getQuantity())
                );
            }
            return result;
        });

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
        OrderRequest orderRequest = new OrderRequest(
            1L,
            Collections.singletonList(orderLineItemRequest)
        );
        OrderResponse actual = orderService.create(orderRequest);

        verify(orderRepository, times(1)).save(any());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getId()).isNotNull()
            .isEqualTo(actual.getOrderLineItems().get(0).getOrderId());
        assertThat(actual.getOrderLineItems()).hasSameSizeAs(orderRequest.getOrderLineItems());
    }

    @DisplayName("주문 항목이 0개인 주문 등록할 경우 예외 처리")
    @Test
    void createWithoutOrderLineItems() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("등록되지 않은 메뉴로 주문 등록할 경우 예외 처리")
    @Test
    void createWithNotFoundMenu() {
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
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
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
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
        when(orderLineItemRepository.findAll()).thenReturn(
            Arrays.asList(orderLineItemOfOrder1, orderLineItemOfOrder2)
        );

        List<OrderResponse> actual = orderService.list();
        List<OrderResponse> expected = OrderResponse.listFrom(
            Arrays.asList(orderLineItemOfOrder1, orderLineItemOfOrder2));

        assertThat(actual).hasSameSizeAs(expected)
            .usingRecursiveFieldByFieldElementComparator()
            .usingElementComparatorIgnoringFields("orderLineItems")
            .hasSameElementsAs(expected);
    }

    @DisplayName("주문 상태 수정")
    @Test
    void changeOrderStatus() {
        long idToChange = order1.getId();
        when(orderRepository.findById(idToChange)).thenReturn(Optional.of(order1));
        when(orderLineItemRepository.findAllByOrder(order1)).thenReturn(
            Collections.singletonList(orderLineItemOfOrder1)
        );

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        OrderResponse actual = orderService.changeOrderStatus(idToChange, orderStatusRequest);
        OrderResponse expected = new OrderResponse(
            order1.getId(),
            orderStatusRequest.getOrderStatus(),
            order1.getOrderedTime(),
            OrderLineItemResponse.listFrom(Collections.singletonList(orderLineItemOfOrder1))
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
                orderTable,
                OrderStatus.COMPLETION
            )
        ));

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(idToChange, orderStatusRequest)
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
