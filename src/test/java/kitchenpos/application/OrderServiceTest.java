package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.name.Name;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderMenu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderResponse.OrderLineItemResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.price.Price;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
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
    private OrderValidator orderValidator;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private OrderService orderService;

    private OrderTable orderTable;
    private Order order1;
    private OrderLineItem orderLineItemOfOrder1;
    private OrderLineItem orderLineItemOfOrder2;
    private Menu menu;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, null, 4, true);
        OrderTable orderTable2 = new OrderTable(2L, null, 4, true);
        new TableGroup(1L, Arrays.asList(orderTable, orderTable2));
        MenuGroup menuGroup = new MenuGroup("치킨");
        menu = new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
            menuGroup.getId(),
            new MenuProducts(Collections.singletonList(
                new MenuProduct(1L, 2L)
            )),
            menuValidator
        );
        order1 = new Order(1L, orderTable.getId(), OrderStatus.COOKING);
        orderLineItemOfOrder1 = new OrderLineItem(order1,
            new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()), 2L);
        Order order2 = new Order(2L, orderTable2.getId(), OrderStatus.COOKING);
        orderLineItemOfOrder2 = new OrderLineItem(order2,
            new OrderMenu(menu.getId(), menu.getName(), menu.getPrice()), 2L);
    }

    @DisplayName("주문 등록")
    @Test
    void create() {
        doNothing().when(orderValidator).validateOrderTable(any());
        when(menuRepository.findAllById(any())).thenReturn(
            Collections.singletonList(menu)
        );
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            return new Order(
                1L,
                order.getOrderTableId(),
                order.getOrderStatus()
            );
        });
        when(orderLineItemRepository.saveAll(any())).thenAnswer(invocation -> {
            List<OrderLineItem> orderLineItems = invocation.getArgument(0);
            List<OrderLineItem> result = new ArrayList<>();
            for (int seq = 1; seq <= orderLineItems.size(); seq++) {
                OrderLineItem orderLineItem = orderLineItems.get(seq - 1);
                result.add(
                    new OrderLineItem((long) seq, orderLineItem.getOrder(),
                        orderLineItem.getOrderMenu(),
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
        doNothing().when(orderValidator).validateOrderTable(any());
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("등록되지 않은 메뉴로 주문 등록할 경우 예외 처리")
    @Test
    void createWithNotFoundMenu() {
        doThrow(IllegalArgumentException.class).when(orderValidator).validateOrderTable(any());

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
        doThrow(IllegalArgumentException.class).when(orderValidator).validateOrderTable(any());

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
                orderTable.getId(),
                OrderStatus.COMPLETION
            )
        ));

        OrderStatusRequest orderStatusRequest = new OrderStatusRequest(OrderStatus.MEAL.name());
        assertThatThrownBy(
            () -> orderService.changeOrderStatus(idToChange, orderStatusRequest)
        ).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
