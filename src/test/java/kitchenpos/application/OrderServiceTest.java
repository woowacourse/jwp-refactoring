package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dto.request.ChangeOrderStatusRequest;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableNotFoundException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends ServiceTestContext {

    @Test
    void 주문_항목이_없다면_예외를_던진다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(savedOrderTable.getId(),
                List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목의_메뉴가_존재하지_않는다면_예외를_던진다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(savedOrderTable.getId(),
                List.of(new OrderLineItemRequest(Long.MAX_VALUE, 1L)));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(MenuNotFoundException.class);
    }

    @Test
    void 주문_테이블이_존재하지_않는다면_예외를_던진다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(Long.MAX_VALUE,
                List.of(new OrderLineItemRequest(savedOrderLineItem.getMenu().getId(), savedOrderLineItem.getQuantity())));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    void 주문은_생성되면_COOKING_상태로_설정된다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(savedOrderTable.getId(),
                List.of(new OrderLineItemRequest(savedOrderLineItem.getMenu().getId(), savedOrderLineItem.getQuantity())));


        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getOrderStatus()).isEqualTo("COOKING");
    }

    @Test
    void 주문을_정상적으로_생성하는_경우_생성한_주문이_반환된다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(savedOrderTable.getId(),
                List.of(new OrderLineItemRequest(savedOrderLineItem.getMenu().getId(), savedOrderLineItem.getQuantity())));


        // when
        OrderResponse response = orderService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 전체_주문을_조회할_수_있다() {
        // given
        CreateOrderRequest request = new CreateOrderRequest(savedOrderTable.getId(),
                List.of(new OrderLineItemRequest(savedOrderLineItem.getMenu().getId(), savedOrderLineItem.getQuantity())));

        orderService.create(request);

        // when
        List<OrderResponse> response = orderService.findAll();

        // then
        assertThat(response).hasSize(2);
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
        Long orderId = savedOrder.getId();
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("COMPLETION");
        orderService.changeOrderStatus(orderId, request);

        // when, then
        assertThatThrownBy(() -> orderService.changeOrderStatus(orderId, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문을_정상적으로_변경하는_경우_변경한_주문이_반환된다() {
        // given
        Long orderId = savedOrder.getId();
        ChangeOrderStatusRequest request = new ChangeOrderStatusRequest("COMPLETION");

        // when
        OrderResponse response = orderService.changeOrderStatus(orderId, request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            assertThat(response.getOrderStatus()).isEqualTo("COMPLETION");
        });
    }
}
