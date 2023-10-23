package kitchenpos.application;

import kitchenpos.application.dto.request.CreateOrderRequest;
import kitchenpos.application.dto.request.OrderLineItemDto;
import kitchenpos.application.dto.request.UpdateOrderStatusRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private Menu chicken = new Menu(1L,
            "chicken",
            BigDecimal.TEN,
            new MenuGroup(1L, "치킨"),
            List.of(new MenuProduct(new Product("치킨", BigDecimal.TEN), 1)));
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

    @Test
    void 주문_시_주문하려는_메뉴를_입력하지_않으면_예외발생() {
        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, Collections.emptyList());
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        then(menuRepository).should(never()).countByIdIn(anyList());
    }

    @Test
    void 주문_시_주문하려는_메뉴가_존재하지_않는_메뉴일_경우_예외발생() {
        // given
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(1L, 3, false)));

        // 존재하지 않는 상품
        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.empty());
        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemDto(1L, 2)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_시_주문하려는_메뉴_간_중복이_있으면_예외발생() {
        // given
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(1L, 3, false)));

        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.of(chicken));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(1L);

        // when
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(
                new OrderLineItemDto(1L, 2), new OrderLineItemDto(1L, 3)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_하는_테이블이_빈_테이블이면_예외발생() {
        // given
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(new OrderTable(1L, 3, true)));

        // when, then
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemDto(1L, 2)));
        assertThatThrownBy(() -> orderService.create(createOrderRequest))
                .isInstanceOf(IllegalArgumentException.class);

        then(orderRepository).should(never()).save(any());
    }

    @Test
    void 주문을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable(1, false);
        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

        given(menuRepository.findById(anyLong()))
                .willReturn(Optional.of(chicken));

        given(menuRepository.countByIdIn(anyList()))
                .willReturn(1L);

        given(orderRepository.save(any(Order.class)))
                .willAnswer(i -> i.getArguments()[0]);

        // when, then
        CreateOrderRequest createOrderRequest = new CreateOrderRequest(1L, List.of(new OrderLineItemDto(1L, 2)));
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
        Order order = new Order();
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
        OrderTable orderTable = new OrderTable(1L, 3, false);
        Order order = new Order(orderTable, OrderStatus.MEAL, LocalDateTime.now());
        order.addOrderLineItem(new OrderLineItem(chicken, 3));

        given(orderRepository.findById(anyLong()))
                .willReturn(Optional.of(order));

        // when
        orderService.changeOrderStatus(1L, new UpdateOrderStatusRequest("COMPLETION"));
    }
}
