package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_ORDER_LINE_ITEM_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.ui.dto.OrderChangeStatusRequest;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrderLineItemsRequest;
import kitchenpos.ui.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

class OrderServiceTest extends KitchenPosServiceTest {

    @DisplayName("Order 생성 - 성공")
    @Test
    void create_Success() {
        OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
            getCreatedMenuId(), TEST_ORDER_LINE_ITEM_QUANTITY);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedNotEmptyOrderTableId(),
            Collections.singletonList(orderLineItemsRequest)
        );

        OrderResponse createdOrder = orderService.create(orderCreateRequest);

        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getOrderTableId()).isEqualTo(orderCreateRequest.getOrderTableId());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(createdOrder.getOrderedTime()).isNotNull();
        for (OrderLineItemResponse createdOrderLineItem : createdOrder.getOrderLineItems()) {
            assertThat(createdOrderLineItem.getSeq()).isNotNull();
        }
    }

    @DisplayName("Order 생성 - 예외 발생, MenuId가 존재하지 않는 경우")
    @Test
    void create_NotExistsMenuId_ThrownException() {
        OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
            getCreatedMenuId() + 1, TEST_ORDER_LINE_ITEM_QUANTITY);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedNotEmptyOrderTableId(),
            Collections.singletonList(orderLineItemsRequest)
        );

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 생성 - 예외 발생, OrderLineItem이 비어있는 경우")
    @Test
    void create_EmptyOrderLineItem_ThrownException() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedNotEmptyOrderTableId(),
            new ArrayList<>()
        );

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 생성 - 예외 발생, OrderLineItem이 Null인 경우")
    @Test
    void create_NullOrderLineItem_ThrownException() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedNotEmptyOrderTableId(),
            null
        );

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 생성 - 예외 발생, 빈 테이블인 경우")
    @Test
    void create_EmptyTable_ThrownException() {
        OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
            getCreatedMenuId(), TEST_ORDER_LINE_ITEM_QUANTITY);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedEmptyOrderTableId(),
            Collections.singletonList(orderLineItemsRequest)
        );

        assertThatThrownBy(() -> orderService.create(orderCreateRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 order 조회 - 성공")
    @Test
    void list_Success() {
        OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
            getCreatedMenuId(), TEST_ORDER_LINE_ITEM_QUANTITY);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
            getCreatedNotEmptyOrderTableId(),
            Collections.singletonList(orderLineItemsRequest)
        );

        OrderResponse createdOrder = orderService.create(orderCreateRequest);

        List<OrderResponse> orders = orderService.list();
        assertThat(orders).isNotNull();
        assertThat(orders).isNotEmpty();
        assertThat(orders).contains(createdOrder);
    }

    @DisplayName("OrderStatus 변경 - 성공, OrderStatus가 Completion이 아닌 상태")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
    void changeOrderStatus_OrderStatusNotCompletion_Success(OrderStatus orderStatus) {
        for (OrderStatus targetOrderStatus : OrderStatus.values()) {

            OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
                getCreatedMenuId(), TEST_ORDER_LINE_ITEM_QUANTITY);

            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                getCreatedNotEmptyOrderTableId(),
                Collections.singletonList(orderLineItemsRequest)
            );

            OrderResponse createdOrder = orderService.create(orderCreateRequest);

            OrderChangeStatusRequest notCompletionRequest = new OrderChangeStatusRequest(
                orderStatus.name());
            orderService.changeOrderStatus(createdOrder.getId(), notCompletionRequest);

            OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest(
                targetOrderStatus.name());
            OrderResponse changedOrder = orderService
                .changeOrderStatus(createdOrder.getId(), orderChangeStatusRequest);

            assertThat(changedOrder.getId()).isNotNull();
            assertThat(changedOrder.getOrderTableId()).isNotNull();
            assertThat(changedOrder.getOrderStatus()).isEqualTo(targetOrderStatus.name());
        }
    }

    @DisplayName("OrderStatus 변경 - 예외 발생, OrderStatus가 Completion 상태")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COMPLETION"})
    void changeOrderStatus_OrderStatusCompletion_ThrownException(OrderStatus orderStatus) {
        for (OrderStatus targetOrderStatus : OrderStatus.values()) {
            OrderLineItemsRequest orderLineItemsRequest = new OrderLineItemsRequest(
                getCreatedMenuId(), TEST_ORDER_LINE_ITEM_QUANTITY);

            OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                getCreatedNotEmptyOrderTableId(),
                Collections.singletonList(orderLineItemsRequest)
            );

            OrderResponse createdOrder = orderService.create(orderCreateRequest);

            OrderChangeStatusRequest completionRequest = new OrderChangeStatusRequest(
                orderStatus.name());
            orderService.changeOrderStatus(createdOrder.getId(), completionRequest);

            OrderChangeStatusRequest orderChangeStatusRequest = new OrderChangeStatusRequest(
                targetOrderStatus.name());
            assertThatThrownBy(() -> orderService
                .changeOrderStatus(createdOrder.getId(), orderChangeStatusRequest))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
