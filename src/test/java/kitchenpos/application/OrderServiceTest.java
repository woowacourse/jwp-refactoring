package kitchenpos.application;

import kitchenpos.order.application.dto.CreateOrderDto;
import kitchenpos.order.application.dto.CreateOrderLineItemDto;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.UpdateOrderStatusDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.order.domain.GuestNumber;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemQuantity;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.menu.exception.MenuException;
import kitchenpos.order.exception.OrderException;
import kitchenpos.order.exception.OrderTableException;
import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTableRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class OrderServiceTest extends MockServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    void 주문_목록을_조회한다() {
        // given
        OrderTable orderTable = new OrderTable(new GuestNumber(10), false);
        Menu chicken = new Menu(new MenuName("chicken"), new MenuPrice(BigDecimal.TEN), null);
        Menu pizza = new Menu(new MenuName("pizza"), new MenuPrice(BigDecimal.ONE), null);

        Order order = new Order(LocalDateTime.now());
        OrderLineItem orderLineItem = new OrderLineItem(chicken, new OrderLineItemQuantity(1L));
        OrderLineItem orderLineItem1 = new OrderLineItem(pizza, new OrderLineItemQuantity(2L));
        order.addOrderLineItems(List.of(orderLineItem, orderLineItem1));
        orderTable.addOrder(order);

        BDDMockito.given(orderRepository.findAllWithOrderLineItems())
                .willReturn(List.of(order));

        // when
        List<OrderDto> actual = orderService.list();

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual.size()).isEqualTo(1);
        softAssertions.assertThat(actual.get(0).getOrderLineItems().size()).isEqualTo(2);
        softAssertions.assertAll();
    }

    @Test
    void 주문을_추가한다() {
        // given
        OrderTable orderTable = new OrderTable(new GuestNumber(10), false);

        Menu chicken = new Menu(new MenuName("chicken"), new MenuPrice(BigDecimal.TEN), null);
        Menu pizza = new Menu(new MenuName("pizza"), new MenuPrice(BigDecimal.ONE), null);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(chicken))
                .willReturn(Optional.of(pizza));
        BDDMockito.given(orderRepository.save(BDDMockito.any(Order.class)))
                .will(next -> next.getArguments()[0]);

        CreateOrderDto createOrderDto = new CreateOrderDto(
                1L,
                List.of(
                        new CreateOrderLineItemDto(1L, 2L),
                        new CreateOrderLineItemDto(2L, 4L)
                ));

        // when
        OrderDto actual = orderService.create(createOrderDto);

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(actual.getOrderLineItems().size()).isEqualTo(2);
        softAssertions.assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        softAssertions.assertAll();
    }

    @Test
    void 주문을_추가할_때_주문_안에_주문아이템_아이디들_값이_비어있으면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(new GuestNumber(10), false);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(orderTable));

        CreateOrderDto createOrderDto = new CreateOrderDto(
                1L,
                Collections.emptyList());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(createOrderDto))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void 주문을_추가할_때_존재하지_않는_주문_메뉴가_있으면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(new GuestNumber(10), false);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        CreateOrderDto createOrderDto = new CreateOrderDto(
                1L,
                List.of(
                        new CreateOrderLineItemDto(1L, 2L),
                        new CreateOrderLineItemDto(2L, 4L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(createOrderDto))
                .isInstanceOf(MenuException.class);
    }

    @Test
    void 주문을_추가할_때_존재하지_않는_주문테이블이면_예외를_던진다() {
        // given
        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        CreateOrderDto createOrderDto = new CreateOrderDto(
                1L,
                List.of(
                        new CreateOrderLineItemDto(1L, 2L),
                        new CreateOrderLineItemDto(2L, 4L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(createOrderDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문을_추가할_때_주문테이블이_주문을_할_수_없는_주문테이블이면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable(new GuestNumber(10), true);

        Menu chicken = new Menu(new MenuName("chicken"), new MenuPrice(BigDecimal.TEN), null);
        Menu pizza = new Menu(new MenuName("pizza"), new MenuPrice(BigDecimal.ONE), null);

        BDDMockito.given(orderTableRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(orderTable));
        BDDMockito.given(menuRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(chicken))
                .willReturn(Optional.of(pizza));

        CreateOrderDto createOrderDto = new CreateOrderDto(
                1L,
                List.of(
                        new CreateOrderLineItemDto(1L, 2L),
                        new CreateOrderLineItemDto(2L, 4L)
                ));

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.create(createOrderDto))
                .isInstanceOf(OrderTableException.class);
    }

    @Test
    void 주문상태를_수정한다() {
        // given
        Order order = new Order(LocalDateTime.now());
        order.changeOrderTable(new OrderTable(new GuestNumber(1), false));
        order.addOrderLineItems(Collections.emptyList());

        BDDMockito.given(orderRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(order));

        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(1L, OrderStatus.MEAL.name());

        // when
        orderService.changeOrderStatus(updateOrderStatusDto);

        // then
        Assertions.assertThat(order.getOrderStatus()).isSameAs(OrderStatus.MEAL);
    }

    @Test
    void 주문상태를_수정할_때_주문_아이디에_해당하는_주문이_없으면_예외를_던진다() {
        // given
        BDDMockito.given(orderRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.empty());

        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(1L, OrderStatus.MEAL.name());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(updateOrderStatusDto))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void 주문상태를_수정할_때_수정하려는_주문의_상태가_COMPLETION_이면_예외를_던진다() {
        // given
        Order order = new Order(LocalDateTime.now());
        order.completeOrder();

        BDDMockito.given(orderRepository.findById(BDDMockito.anyLong()))
                .willReturn(Optional.of(order));

        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(1L, OrderStatus.MEAL.name());

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(updateOrderStatusDto))
                .isInstanceOf(OrderException.class);
    }

    @Test
    void 주문상태를_수정할_때_수정하려는_주문의_상태가_존재하지_않는_주문상태이면_예외를_던진다() {
        // given
        UpdateOrderStatusDto updateOrderStatusDto = new UpdateOrderStatusDto(1L, "NoSuchOrderStatus");

        // when, then
        Assertions.assertThatThrownBy(() -> orderService.changeOrderStatus(updateOrderStatusDto))
                .isInstanceOf(OrderException.class);
    }
}
