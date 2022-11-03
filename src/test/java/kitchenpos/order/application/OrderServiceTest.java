package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.request.MenuGroupRequest;
import kitchenpos.menu.application.request.MenuProductRequest;
import kitchenpos.menu.application.request.MenuRequest;
import kitchenpos.order.application.request.OrderLineItemRequest;
import kitchenpos.order.application.request.OrderRequest;
import kitchenpos.order.application.request.OrderTableRequest;
import kitchenpos.order.application.response.OrderResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.application.request.ProductCreateRequest;

public class OrderServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문을 생성한다.")
    void create() {
        // given
        OrderRequest request = createOrderRequest();

        // when
        OrderResponse savedOrder = orderService.create(request);

        // then
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    @Test
    @DisplayName("OrderLineItem 이 empty 인 경우 예외를 던진다.")
    void validateOrderLineItems() {
        // given
        OrderRequest request = createOrderRequest(List.of());

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("OrderTable 이 존재하지 않는 경우 예외를 던진다.")
    void validateOrderTableNotEmpty() {
        // given
        OrderLineItemRequest orderLineItemRequest = createOrderLineItemRequest();
        OrderRequest request = new OrderRequest(NO_ID, 9999L, OrderStatus.COOKING.name(),
            List.of(orderLineItemRequest));

        // when, then
        assertThatThrownBy(() -> orderService.create(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("order table not found");
    }

    @Test
    @DisplayName("전체 주문을 조회한다.")
    void list() {
        // given
        OrderResponse savedOrder = orderService.create(createOrderRequest());

        // when
        List<OrderResponse> result = orderService.list();

        // then
        assertThat(result).hasSize(1);
        assertOrderResponse(result.get(0), savedOrder);
    }

    @ParameterizedTest
    @ValueSource(strings = {"MEAL", "COMPLETION"})
    @DisplayName("주문 상태를 변경한다.")
    void changeOrderStatus_meal(String orderStatus) {
        // given
        OrderResponse order = orderService.create(createOrderRequest());
        OrderRequest changeRequest = new OrderRequest(NO_ID, NO_ID, orderStatus, null);

        // when
        orderService.changeOrderStatus(order.getId(), changeRequest);
        Order changedOrder = orderDao.findById(order.getId()).orElseThrow();

        // then
        assertThat(changedOrder.getOrderStatus()).isEqualTo(orderStatus);
    }

    private OrderRequest createOrderRequest() {
        return createOrderRequest(List.of(createOrderLineItemRequest()));
    }

    private OrderRequest createOrderRequest(final List<OrderLineItemRequest> orderLineItemRequests) {
        OrderTableRequest orderTable = new OrderTableRequest(NO_ID, NO_ID, 1, false);
        long tableId = tableService.create(orderTable).getId();

        return new OrderRequest(NO_ID, tableId, OrderStatus.COOKING.name(), orderLineItemRequests);
    }

    private OrderLineItemRequest createOrderLineItemRequest() {
        MenuGroupRequest menuGroup = new MenuGroupRequest(NO_ID, "세마리메뉴");
        long menuGroupId = menuGroupService.create(menuGroup).getId();

        ProductCreateRequest product = new ProductCreateRequest("후라이드", BigDecimal.valueOf(16000));
        long productId = productService.create(product).getId();

        MenuRequest menu = new MenuRequest(NO_ID, "후라이드+후라이드+후라이드", new BigDecimal(48000), menuGroupId,
            List.of(new MenuProductRequest(NO_ID, NO_ID, productId, 3)));
        long menuId = menuService.create(menu).getId();

        return new OrderLineItemRequest(NO_ID, NO_ID, menuId, 1);
    }

    private void assertOrderResponse(final OrderResponse actual, final OrderResponse expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getOrderTableId()).isEqualTo(expected.getOrderTableId());
        assertThat(actual.getOrderedTime().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")))
            .isEqualTo(expected.getOrderedTime().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")));
        assertThat(actual.getOrderStatus()).isEqualTo(expected.getOrderStatus());
    }
}
