package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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
    OrderService orderService;

    @Test
    @DisplayName("주문을 생성할 수 있다.")
    void create() {
        // given
        Order expected = new Order();
        long orderId = 1L;
        expected.setId(orderId);

        MenuGroup menuGroup = new MenuGroup();
        long menuId = 1L;
        Menu menu = new Menu(menuId, "name", BigDecimal.valueOf(1000L), menuGroup);
        long menuQuantity = 1L;

        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setOrder(expected);
        orderLineItem.setQuantity(menuQuantity);

        expected.setOrderLineItems(Arrays.asList(orderLineItem));

        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setEmpty(false);

        expected.setOrderTableId(orderTable.getId());

        given(menuRepository.countByIdIn(any(List.class)))
                .willReturn(Long.valueOf(expected.getOrderLineItems().size()));
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.of(menu));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.save(any(Order.class)))
                .willReturn(expected);
        given(orderLineItemRepository.save(any(OrderLineItem.class)))
                .willReturn(orderLineItem);

        // when
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);
        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));
        Order actual = orderService.create(orderRequest);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("주문의 항목이 비어있는 경우 예외가 발생한다.")
    void createFailWhenEmptyOrderLineItems() {
        // given
        OrderRequest orderRequest = new OrderRequest(null, Collections.emptyList());

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 항목의 메뉴를 찾을 수 없는 경우 예외가 발생한다.")
    void createFailWhenCannotFindMenu() {
        // given
        OrderRequest orderRequest = new OrderRequest(null, Arrays.asList(new OrderLineItemRequest(1L, 1L)));

        given(menuRepository.countByIdIn(any(List.class)))
                .willReturn(0L);

        // when
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);

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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블에 주문 생성 시 예외가 발생한다.")
    void createFailWhenPlaceOrderOnEmptyTable() {
        // given
        Long menuId = 1L;
        Long menuQuantity = 1L;
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(menuId, menuQuantity);

        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        orderTable.setEmpty(true);

        OrderRequest orderRequest = new OrderRequest(orderTableId, Arrays.asList(orderLineItemRequest));

        given(menuRepository.countByIdIn(any(List.class)))
                .willReturn(Long.valueOf(orderRequest.getOrderLineItemRequests().size()));
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 목록을 불러올 수 있다.")
    void list() {
        // given
        Order order1 = new Order();
        Order order2 = new Order();

        List<Order> expected = Arrays.asList(order1, order2);

        given(orderRepository.findAll())
                .willReturn(expected);
        // when
        List<Order> actual = orderService.list();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("주문을 찾지 못하면 예외가 발생한다.")
    void changeOrderStatusFailWhenCannotFindOrder() {
        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문이 이미 완료된 상태라면 상태를 변경할 수 없다.")
    void changeOrderStatusFailWhenOrderIsCompleted() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderStatus(OrderStatus.COMPLETION.name());

        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 진행 상황을 변경할 수 있다.")
    void changeOrderStatus() {
        // given
        Order order = new Order();
        order.setId(1L);

        OrderStatus expectedStatus = OrderStatus.MEAL;
        order.setOrderStatus(expectedStatus.name());

        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);

        // when
        Order actual = orderService.changeOrderStatus(order.getId(), order);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expectedStatus.name());
    }
}
