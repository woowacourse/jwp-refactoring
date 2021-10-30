package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.menu.EmptyOrderLineItemsException;
import kitchenpos.exception.menu.NoSuchMenuException;
import kitchenpos.exception.order.CannotChangeOrderStatusAsCompletionException;
import kitchenpos.exception.order.CannotPlaceAnOrderAsTableIsEmptyException;
import kitchenpos.exception.order.NoSuchOrderException;
import kitchenpos.exception.table.NoSuchOrderTableException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private MenuRepository menuRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private OrderLineItemRepository orderLineItemRepository;

    @MockBean
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderService orderService;

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(1L, null, 1, false);

        Menu mockMenu = mock(Menu.class);
        Long menuQuantity = 1L;

        OrderLineItem orderLineItem = new OrderLineItem(mockMenu, menuQuantity);
        Order expected = new Order(orderTable, Arrays.asList(orderLineItem));

        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, menuQuantity);
        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));

        given(menuRepository.countByIdIn(any(List.class)))
                .willReturn(Long.valueOf(expected.getOrderLineItems().size()));
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.of(mockMenu));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.save(any(Order.class)))
                .willReturn(expected);
        given(orderLineItemRepository.save(any(OrderLineItem.class)))
                .willReturn(orderLineItem);

        // when

        OrderResponse actual = orderService.create(orderRequest);

        // then
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("orderedTime")
                .isEqualTo(actual);
    }

    @Test
    @DisplayName("주문의 항목이 비어있는 경우 예외가 발생한다.")
    void createFailWhenEmptyOrderLineItems() {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, Collections.emptyList());

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(OrderTable.class)));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(EmptyOrderLineItemsException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴를 찾을 수 없는 경우 예외가 발생한다.")
    void createFailWhenCannotFindMenu() {
        // given
        OrderRequest orderRequest = new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 1L)));

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(OrderTable.class)));
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NoSuchMenuException.class);

    }

    @Test
    @DisplayName("테이블을 찾을 수 없는 경우 예외가 발생한다.")
    void createFailWhenCannotFindOrderTable() {
        // given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(mock(OrderLineItemRequest.class));
        OrderRequest orderRequest = new OrderRequest(1L, orderLineItemRequests);

        given(menuRepository.countByIdIn(any(List.class)))
                .willReturn(Long.valueOf(orderLineItemRequests.size()));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NoSuchOrderTableException.class);
    }

    @Test
    @DisplayName("빈 테이블에 주문 생성 시 예외가 발생한다.")
    void createFailWhenPlaceOrderOnEmptyTable() {
        // given
        Long menuId = 1L;
        Long menuQuantity = 1L;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);

        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, null, 1, true);

        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));

        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.of(mock(Menu.class)));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(CannotPlaceAnOrderAsTableIsEmptyException.class);
    }

    @Test
    @DisplayName("주문 목록을 불러올 수 있다.")
    void list() {
        // given
        OrderTable mockOrderTable = mock(OrderTable.class);
        Menu mockMenu = mock(Menu.class);
        OrderLineItem orderLineItem = new OrderLineItem(mockMenu, 1L);
        Order order = new Order(1L,
                mockOrderTable,
                OrderStatus.COMPLETION.name(),
                LocalDateTime.now(),
                Arrays.asList(orderLineItem));

        given(mockOrderTable.getId())
                .willReturn(1L);
        given(mockMenu.getId())
                .willReturn(1L);

        List<Order> expectedOrders = Arrays.asList(order, order);
        given(orderRepository.findAll())
                .willReturn(expectedOrders);

        List<OrderResponse> expected = expectedOrders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("주문을 찾지 못하면 예외가 발생한다.")
    void changeOrderStatusFailWhenCannotFindOrder() {
        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, mock(OrderRequest.class)))
                .isInstanceOf(NoSuchOrderException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태라면 상태를 변경할 수 없다.")
    void changeOrderStatusFailWhenOrderIsCompleted() {
        Order order = new Order(1L, mock(OrderTable.class), OrderStatus.COMPLETION.name(), LocalDateTime.now(), Arrays.asList(mock(OrderLineItem.class)));

        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, mock(OrderRequest.class)))
                .isInstanceOf(CannotChangeOrderStatusAsCompletionException.class);
    }

    @Test
    @DisplayName("주문의 주문 상태을 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        OrderTable orderTable = new OrderTable(1L, mock(TableGroup.class), 1, false);
        Menu mockMenu = mock(Menu.class);
        Order order = new Order(1L,
                orderTable,
                OrderStatus.COOKING.name(),
                LocalDateTime.now(),
                Arrays.asList(new OrderLineItem(mockMenu, 1L)));

        given(mockMenu.getId())
                .willReturn(1L);
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);

        OrderStatus expectedStatus = OrderStatus.MEAL;
        OrderRequest orderRequest = new OrderRequest(expectedStatus.name());

        // when
        OrderResponse actual = orderService.changeOrderStatus(order.getId(), orderRequest);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expectedStatus.name());
    }
}
