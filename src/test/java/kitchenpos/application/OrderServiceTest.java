package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.Fixtures;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderLineItemService;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.application.OrderTableService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    private OrderLineItem orderLineItem;
    private List<OrderLineItem> orderLineItems;
    private Order order;
    private OrderTable orderTable;
    private Menu menu;

    @Mock
    private MenuService menuService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemService orderLineItemService;

    @Mock
    private OrderTableService orderTableService;

    @BeforeEach
    void setUp() {
        TableGroup tableGroup = new TableGroup(1L);
        orderTable = new OrderTable(1L, tableGroup.getId(), 1, false);
        order = new Order(orderTable.getId(), OrderStatus.COOKING);
        menu = Fixtures.makeMenu();

        orderLineItem = new OrderLineItem(1L, order, menu.getId(), 1L);

        orderLineItems = new ArrayList<>();
        orderLineItems.add(orderLineItem);

        order.addOrderLineItems(orderLineItems);

    }

    @DisplayName("order 생성")
    @Test
    void create() {
        given(orderRepository.save(any(Order.class)))
            .willReturn(order);

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1);

        OrderRequest orderRequest =
            new OrderRequest(
                1L,
                OrderStatus.COOKING,
                Collections.singletonList(orderLineItemRequest));

        orderService.create(orderRequest);

        verify(orderRepository).save(any(Order.class));
        verify(orderLineItemService).saveAll(anyList());
    }

    @DisplayName("order 불러오기")
    @Test
    void list() {
        List<Order> orders = new ArrayList<>();
        orders.add(this.order);

        given(orderRepository.findAll())
            .willReturn(orders);

        orderService.list();

        verify(orderRepository).findAll();
    }

    @DisplayName("주문상태 바꾸기")
    @Test
    void changeOrder() {
        given(orderRepository.findById(anyLong()))
            .willReturn(Optional.of(order));

        OrderRequest orderRequest = new OrderRequest(1L, OrderStatus.COOKING, null);

        orderService.changeOrderStatus(1L, orderRequest);
    }

}
