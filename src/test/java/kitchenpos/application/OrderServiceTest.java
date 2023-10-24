package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.request.OrderRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import kitchenpos.dto.response.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @DisplayName("새로운 주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable = new OrderTable(1000L, tableGroup, 2, false);
        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), 1L);
        final Menu menu2 = new Menu(11L, "후라이드 간장 세트", new Price(BigDecimal.valueOf(30000)), 1L);

        final OrderRequest orderRequest = new OrderRequest(
                orderTable.getId(),
                OrderStatus.COOKING.name(),
                List.of(new OrderLineItemRequest(menu1.getId(), 1),
                        new OrderLineItemRequest(menu2.getId(), 1))
        );

        final Order order = new Order(1L, null, OrderStatus.COOKING.name());

        final OrderLineItem orderLineItem1 = new OrderLineItem(order, menu1.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(order, menu2.getId(), 1);
        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);

        given(menuRepository.countByIdIn(any()))
                .willReturn(2L);

        final Order updatedOrder = new Order(
                order.getId(),
                orderTable.getId(),
                order.getOrderStatus(),
                order.getOrderLineItems()
        );

        given(orderTableRepository.findById(any()))
                .willReturn(Optional.of(orderTable));

        given(orderRepository.save(any()))
                .willReturn(updatedOrder);

        // when & then
        assertThat(orderService.create(orderRequest)).isEqualTo(updatedOrder.getId());
        then(menuRepository).should(times(1)).countByIdIn(any());
        then(orderTableRepository).should(times(1)).findById(anyLong());
        then(orderRepository).should(times(1)).save(any());
        then(orderLineItemRepository).should(times(2)).save(any());
    }

    @DisplayName("주문 항목에 존재하는 메뉴가 존재하지 않는 메뉴이면 등록할 수 없다.")
    @Test
    void create_FailWhenMenuNotExist() {
        // given
        final Order order = new Order(1L, 100L, OrderStatus.COOKING.name());

        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), 1L);

        final OrderRequest orderRequest = new OrderRequest(
                100L,
                OrderStatus.COOKING.name(),
                List.of(new OrderLineItemRequest(menu1.getId(), 1))
        );

        final OrderLineItem orderLineItem1 = new OrderLineItem(order, menu1.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(order, 11L, 1);

        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품이 존재하지 않습니다.");
    }

    @DisplayName("주문 테이블의 상태가 비어있으면 등록할 수 없다.")
    @Test
    void create_FailWhenOrderTableIsEmpty() {
        // given
        final Order order = new Order(1L, 100L, OrderStatus.COOKING.name());

        final Menu menu1 = new Menu(10L, "후라이드 양념 세트", new Price(BigDecimal.valueOf(30000)), 1L);
        final Menu menu2 = new Menu(11L, "후라이드 간장 세트", new Price(BigDecimal.valueOf(30000)), 1L);

        final OrderRequest orderRequest = new OrderRequest(
                100L,
                OrderStatus.COOKING.name(),
                List.of(new OrderLineItemRequest(menu1.getId(), 1),
                        new OrderLineItemRequest(menu2.getId(), 1))
        );

        final OrderLineItem orderLineItem1 = new OrderLineItem(order, menu1.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(order, menu2.getId(), 1);

        order.addOrderLineItem(orderLineItem1);
        order.addOrderLineItem(orderLineItem2);

        given(menuRepository.countByIdIn(any()))
                .willReturn(2L);

        final TableGroup tableGroup = new TableGroup(10L);
        final OrderTable orderTable = new OrderTable(1000L, tableGroup, 2, true);

        final Order udpatedOrder = new Order(
                order.getId(),
                orderTable.getId(),
                order.getOrderStatus(),
                order.getOrderLineItems()
        );

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블의 상태가 비어 있습니다.");
    }

    @DisplayName("주문 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final Order order1 = new Order(1L, null, OrderStatus.COOKING.name());
        final Order order2 = new Order(2L, null, OrderStatus.COOKING.name());
        final List<Order> orders = List.of(order1, order2);

        final List<OrderResponse> responses = orders.stream()
                .map(OrderResponse::from)
                .collect(Collectors.toUnmodifiableList());

        given(orderRepository.findAll())
                .willReturn(orders);

        // when & then
        assertThat(orderService.list()).usingRecursiveComparison().isEqualTo(responses);
        then(orderRepository).should(times(1)).findAll();
    }

    @DisplayName("특정 주문의 상태를 변경할 수 있다.")
    @Test
    void changeOrderStatus() {
        // given
        final Order order = new Order(1L, null, OrderStatus.COOKING.name());
        final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());
        final Order updatedOrder = new Order(1L, null, OrderStatus.MEAL.name());

        given(orderRepository.findById(1L))
                .willReturn(Optional.of(order));

        given(orderRepository.save(any()))
                .willReturn(updatedOrder);

        // when
        orderService.changeOrderStatus(1L, updateRequest);

        // then
        then(orderRepository).should(times(1)).findById(1L);
        then(orderRepository).should(times(1)).save(any());
    }

    @DisplayName("해당 주문이 존재하지 않으면 변경할 수 없다.")
    @Test
    void changeOrderStatus_FailWhenOrderNotExist() {
        // given
        final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(OrderStatus.MEAL.name());
        final Order order = new Order(1L, null, OrderStatus.MEAL.name());

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(2L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문입니다.");
    }

    @DisplayName("해당 주문의 상태가 이미 완료이면 변경할 수 없다.")
    @Test
    void changeOrderStatus_FailWhenOrderStatusAlreadyCompletion() {
        // given
        final OrderStatusUpdateRequest updateRequest = new OrderStatusUpdateRequest(OrderStatus.COMPLETION.name());
        final Order order = new Order(1L, null, OrderStatus.COMPLETION.name());

        given(orderRepository.findById(1L))
                .willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, updateRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 완료된 주문입니다.");
    }
}
