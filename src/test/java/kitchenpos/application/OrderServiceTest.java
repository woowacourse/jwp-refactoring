package kitchenpos.application;

import static kitchenpos.constants.Constants.TEST_ORDER_LINE_ITEM_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends KitchenPosServiceTest {

    @Autowired
    private OrderService orderService;

    @DisplayName("Order 생성 - 성공")
    @Test
    void create_Success() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

        Order order = new Order();
        order.setOrderTableId(getCreatedNotEmptyOrderTableId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        Order createdOrder = orderService.create(order);

        assertThat(createdOrder.getId()).isNotNull();
        assertThat(createdOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        assertThat(createdOrder.getOrderedTime()).isNotNull();
        for (OrderLineItem createdOrderLineItem : createdOrder.getOrderLineItems()) {
            assertThat(createdOrderLineItem.getSeq()).isNotNull();
        }
    }

    @DisplayName("Order 생성 - 예외 발생, MenuId가 존재하지 않는 경우")
    @Test
    void create_NotExistsMenuId_ThrownException() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(getCreatedMenuId() + 1);
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

        Order order = new Order();
        order.setOrderTableId(getCreatedNotEmptyOrderTableId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 생성 - 예외 발생, OrderLineItem이 비어있는 경우")
    @Test
    void create_EmptyOrderLineItem_ThrownException() {
        OrderLineItem orderLineItem = new OrderLineItem();

        Order order = new Order();
        order.setOrderTableId(getCreatedNotEmptyOrderTableId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Order 생성 - 예외 발생, 빈 테이블인 경우")
    @Test
    void create_EmptyTable_ThrownException() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

        Order order = new Order();
        order.setOrderTableId(getCreatedEmptyOrderTableId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        assertThatThrownBy(() -> orderService.create(order))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 order 조회 - 성공")
    @Test
    void list_Success() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(getCreatedMenuId());
        orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

        Order order = new Order();
        order.setOrderTableId(getCreatedNotEmptyOrderTableId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));
        Order createdOrder = orderService.create(order);

        List<Order> orders = orderService.list();
        assertThat(orders).isNotNull();
        assertThat(orders).isNotEmpty();

        List<Long> orderIds = orders.stream()
            .map(Order::getId)
            .collect(Collectors.toList());
        assertThat(orderIds).contains(createdOrder.getId());
    }

    @DisplayName("OrderStatus 변경 - 성공, OrderStatus가 Completion이 아닌 상태")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COMPLETION"}, mode = Mode.EXCLUDE)
    void changeOrderStatus_OrderStatusNotCompletion_Success(OrderStatus orderStatus) {
        for (OrderStatus targetOrderStatus : OrderStatus.values()) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(getCreatedMenuId());
            orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

            Order order = new Order();
            order.setOrderTableId(getCreatedNotEmptyOrderTableId());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));
            Order createdOrder = orderService.create(order);

            Order orderOnlyStatusNotComplete = new Order();
            orderOnlyStatusNotComplete.setOrderStatus(orderStatus.name());
            orderService.changeOrderStatus(createdOrder.getId(), orderOnlyStatusNotComplete);

            Order orderOnlyStatus = new Order();
            orderOnlyStatus.setOrderStatus(targetOrderStatus.name());
            Order changedOrder = orderService
                .changeOrderStatus(createdOrder.getId(), orderOnlyStatus);

            assertThat(changedOrder.getId()).isNotNull();
            assertThat(changedOrder.getOrderTableId());
        }
    }

    @DisplayName("OrderStatus 변경 - 예외 발생, OrderStatus가 Completion 상태")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COMPLETION"})
    void changeOrderStatus_OrderStatusCompletion_ThrownException(OrderStatus orderStatus) {
        for (OrderStatus targetOrderStatus : OrderStatus.values()) {
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(getCreatedMenuId());
            orderLineItem.setQuantity(TEST_ORDER_LINE_ITEM_QUANTITY);

            Order order = new Order();
            order.setOrderTableId(getCreatedNotEmptyOrderTableId());
            order.setOrderLineItems(Collections.singletonList(orderLineItem));
            Order createdOrder = orderService.create(order);

            Order orderOnlyStatusNotComplete = new Order();
            orderOnlyStatusNotComplete.setOrderStatus(orderStatus.name());
            orderService.changeOrderStatus(createdOrder.getId(), orderOnlyStatusNotComplete);

            Order orderOnlyStatus = new Order();
            orderOnlyStatus.setOrderStatus(targetOrderStatus.name());
            assertThatThrownBy(
                () -> orderService.changeOrderStatus(createdOrder.getId(), orderOnlyStatus))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
