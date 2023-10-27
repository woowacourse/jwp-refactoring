package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.event.ValidateMenuExistsEvent;
import kitchenpos.order.event.ValidateOrderTableIsNotEmptyEvent;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.request.ChangeOrderStatusRequest;
import kitchenpos.order.dto.request.CreateOrderRequest;
import kitchenpos.order.dto.request.OrderLineItemRequest;
import kitchenpos.order.dto.response.OrderResponse;
import kitchenpos.order.exception.OrderIsCompletedException;
import kitchenpos.order.exception.OrderLineEmptyException;
import kitchenpos.order.exception.OrderNotFoundException;
import kitchenpos.supports.ServiceTestContext;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.exception.OrderTableNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTestContext {

    @Test
    void 주문_항목이_없다면_예외를_던진다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(OrderLineEmptyException.class);
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외를_던진다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(Long.MAX_VALUE, 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외를_던진다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        menuRepository.save(menu);

        CreateOrderRequest request = new CreateOrderRequest(Long.MAX_VALUE,
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 주문은_생성되면_COOKING_상태로_설정된다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);

        menuRepository.save(menu);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    void 주문을_정상적으로_생성하는_경우_생성한_주문이_반환된다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);

        menuRepository.save(menu);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 전체_주문을_조회할_수_있다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);

        menuRepository.save(menu);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        orderService.create(request);

        // when
        List<OrderResponse> response = orderService.findAll();

        // then
        assertThat(response).hasSize(1);
    }

    @Test
    void 주문이_존재하지_않으면_상태를_변경하려_할_때_예외를_던진다() {
        // given
        Long orderId = Long.MAX_VALUE;
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("MEAL");

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void 이미_완료된_주문이라면_상태를_변경할_수_없다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        Order order = OrderFixture.of(orderTable.getId(), OrderStatus.MEAL, LocalDateTime.now());
        orderRepository.save(order);

        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("COMPLETION");
        orderService.changeOrderStatus(order.getId(), request);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), request))
                .isInstanceOf(OrderIsCompletedException.class);
    }

    @Test
    void 주문을_정상적으로_변경하는_경우_변경한_주문이_반환된다() {
        // given
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);
        orderTableRepository.save(orderTable);

        Order order = OrderFixture.of(orderTable.getId(), OrderStatus.MEAL, LocalDateTime.now());
        orderRepository.save(order);

        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("COMPLETION");

        // when
        OrderResponse response = orderService.changeOrderStatus(order.getId(), request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            assertThat(response.getOrderStatus()).isEqualTo("COMPLETION");
        });
    }

    @Test
    void 주문을_생성할_때_테이블이_비었는지_검증하는_이벤트를_발행한다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);

        menuRepository.save(menu);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // when
        orderService.create(request);

        // then
        long eventOccurredCount = applicationEvents.stream(ValidateOrderTableIsNotEmptyEvent.class)
                .count();

        assertThat(eventOccurredCount).isEqualTo(1);
    }

    @Test
    void 주문을_생성할_때_메뉴가_존재하는지_검증하는_이벤트를_발행한다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        menuGroupRepository.save(menuGroup);

        Menu menu = MenuFixture.of(menuGroup.getId(), "name", BigDecimal.valueOf(1000L));
        OrderTable orderTable = OrderTableFixture.of(null, 1, false);

        menuRepository.save(menu);
        orderTableRepository.save(orderTable);

        CreateOrderRequest request = new CreateOrderRequest(orderTable.getId(),
                List.of(new OrderLineItemRequest(menu.getId(), 1L)));

        // when
        orderService.create(request);

        // then
        long eventOccurredCount = applicationEvents.stream(ValidateMenuExistsEvent.class)
                .count();

        assertThat(eventOccurredCount).isEqualTo(1);
    }
}
