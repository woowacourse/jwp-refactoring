package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.OrderLineItemRequest;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderLineItemRepository orderLineItemRepository;
    @Mock
    private OrderValidator orderValidator;

    @Mock
    OrderLineItemValidator orderLineItemValidator;

    @Mock
    OrderMapper orderMapper;

    @Mock
    OrderLineItemsMapper orderLineItemsMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문_시_주문하려는_메뉴를_입력하지_않으면_예외발생() {
        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, Collections.emptyList());
        willThrow(IllegalArgumentException.class)
                .given(orderLineItemsMapper).toOrderLineItems(any(), any());

        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문_시_주문하려는_메뉴가_존재하지_않는_메뉴일_경우_예외발생() {
        // given
        willThrow(IllegalArgumentException.class)
                .given(orderLineItemsMapper).toOrderLineItems(any(), any());
        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemRequest(1L, 2)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문_시_주문하려는_메뉴_간_중복이_있으면_예외발생() {
        // given
        willThrow(IllegalArgumentException.class)
                .given(orderLineItemsMapper).toOrderLineItems(any(), any());

        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(
                new OrderLineItemRequest(1L, 2), new OrderLineItemRequest(1L, 3)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문_하는_테이블이_빈_테이블이면_예외발생() {
        // given
        willThrow(IllegalArgumentException.class)
                .given(orderMapper).toOrder(anyLong(), any());

        // when, then
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemRequest(1L, 2)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable(1, false);

        given(orderMapper.toOrder(anyLong(), any()))
                .willReturn(new Order(1L));

        given(orderLineItemsMapper.toOrderLineItems(any(), any()))
                .willReturn(List.of(new OrderLineItem(1L, "치킨", BigDecimal.TEN), new OrderLineItem(3, "피자", BigDecimal.ONE)));

        given(orderRepository.save(any(Order.class)))
                .willAnswer(i -> i.getArguments()[0]);

        // when, then
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemRequest(1L, 2)));
        orderService.create(createOrderRequest);
        then(orderRepository).should(times(1)).save(any());
    }

    @Test
    void 주문_상태_변경_시_변경하려는_주문이_존재하지_않으면_예외발생() {
        // given
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new UpdateOrderStatusRequest("MEAL")))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 상태를_변경하려는_주문이_이미_완료_상태면_예외발생() {
        // given
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new UpdateOrderStatusRequest("MEAL")))
                .isInstanceOf(IllegalArgumentException.class);
        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문_상태를_변경한다() {
        // given
        Order order = new Order(1L);
        order.changeOrderStatus(OrderStatus.MEAL);

        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when
        assertDoesNotThrow(() -> orderService.changeOrderStatus(1L, new UpdateOrderStatusRequest("COMPLETION")));
    }
}
