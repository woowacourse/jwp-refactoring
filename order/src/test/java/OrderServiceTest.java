import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.application.OrderService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Order;
import kitchenpos.menu.domain.OrderLineItem;
import kitchenpos.menu.domain.OrderLineItemRepository;
import kitchenpos.menu.domain.OrderMenu;
import kitchenpos.menu.domain.OrderRepository;
import kitchenpos.menu.domain.OrderStatus;
import kitchenpos.menu.domain.OrderTable;
import kitchenpos.menu.domain.TableGroup;
import kitchenpos.menu.dto.OrderRequest;
import kitchenpos.menu.dto.OrderRequest.OrderLineItemRequest;
import kitchenpos.menu.dto.OrderResponse;
import kitchenpos.menu.dto.OrderResponse.OrderLineItemResponse;
import kitchenpos.menu.dto.OrderStatusRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

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
        Long menuGroupId = 1L;
        menu = new Menu(1L, new Name("후라이드치킨"), new Price(BigDecimal.valueOf(16000)),
            menuGroupId,
            new MenuProducts(Collections.singletonList(
                new MenuProduct(1L, 2L)
            ))
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
        Mockito.when(menuRepository.findAllById(ArgumentMatchers.any())).thenReturn(
            Collections.singletonList(menu)
        );
        Mockito.when(orderRepository.save(ArgumentMatchers.any(Order.class)))
            .thenAnswer(invocation -> {
                Order order = invocation.getArgument(0);
                return new Order(
                    1L,
                    order.getOrderTableId(),
                    order.getOrderStatus()
                );
            });
        Mockito.when(orderLineItemRepository.saveAll(ArgumentMatchers.any()))
            .thenAnswer(invocation -> {
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

        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(actual.getId()).isNotNull()
            .isEqualTo(actual.getOrderLineItems().get(0).getOrderId());
        assertThat(actual.getOrderLineItems()).hasSameSizeAs(orderRequest.getOrderLineItems());
    }

    @DisplayName("주문 항목이 0개인 주문 등록할 경우 예외 처리")
    @Test
    void createWithoutOrderLineItems() {
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        assertThatThrownBy(() -> orderService.create(orderRequest)).isExactlyInstanceOf(
            IllegalArgumentException.class
        );
    }

    @DisplayName("주문 조회")
    @Test
    void list() {
        Mockito.when(orderLineItemRepository.findAll()).thenReturn(
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
        Mockito.when(orderRepository.findById(idToChange)).thenReturn(Optional.of(order1));
        Mockito.when(orderLineItemRepository.findAllByOrder(order1)).thenReturn(
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

    @DisplayName("계산 완료 상태 주문의 상태 수정시 예외 처리")
    @Test
    void changeOrderStatusWith() {
        long idToChange = 1L;
        Mockito.when(orderRepository.findById(idToChange)).thenReturn(Optional.of(
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
