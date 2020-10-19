package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

class OrderServiceIntegrationTest extends ServiceIntegrationTest {
    @Autowired
    OrderService orderService;

    @DisplayName("주문을 추가한다.")
    @Test
    void create() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(2);
        Order order = new Order();
        order.setOrderTableId(2L);
        order.setOrderLineItems(Arrays.asList(orderLineItem));

        Order persist = orderService.create(order);

        assertAll(
            () -> assertThat(persist).isEqualToIgnoringGivenFields(order, "id", "orderLineItems"),
            () -> assertThat(persist.getOrderLineItems().get(0)).isEqualToIgnoringNullFields(orderLineItem)
        );
    }

    @DisplayName("주문 전체를 조회한다.")
    @Test
    void list() {
        OrderLineItem twoFriedChicken = getOrderLineItem(1L, 2);
        Order twoFriedOrder = getOrder(2L, Arrays.asList(twoFriedChicken));
        orderService.create(twoFriedOrder);
        OrderLineItem halfAndHalfChicken = getOrderLineItem(3L, 1);
        Order halfAndHalfOrder = getOrder(2L, Arrays.asList(halfAndHalfChicken));
        orderService.create(halfAndHalfOrder);

        List<Order> orders = orderService.list();

        assertAll(
            () -> assertThat(orders).hasSize(2),
            () -> assertThat(orders.get(0)).isEqualToIgnoringGivenFields(twoFriedOrder, "id", "orderLineItems"),
            () -> assertThat(orders.get(0).getOrderLineItems().get(0)).isEqualToIgnoringNullFields(twoFriedChicken),
            () -> assertThat(orders.get(1)).isEqualToIgnoringGivenFields(halfAndHalfOrder, "id", "orderLineItems"),
            () -> assertThat(orders.get(1).getOrderLineItems().get(0)).isEqualToIgnoringNullFields(halfAndHalfChicken)

        );
    }

    @DisplayName("주문의 주문 상태를 변경한다.")
    @Test
    void changeOrderStatus() {
        OrderLineItem twoFriedChicken = getOrderLineItem(1L, 2);
        Order twoFriedOrder = getOrder(2L, Arrays.asList(twoFriedChicken));
        Order persist = orderService.create(twoFriedOrder);

        Order changedOrder = orderService.changeOrderStatus(persist.getId(), getOrderWithCookingStatus());

        List<Long> orderLineItemsSeqs = persist.getOrderLineItems().stream()
            .map(OrderLineItem::getSeq)
            .collect(Collectors.toList());

        assertAll(
            () -> assertThat(changedOrder).isEqualToIgnoringGivenFields(persist, "orderStatus", "orderLineItems"),
            () -> assertThat(changedOrder.getOrderStatus()).isEqualTo(ORDER_STATUS_COOKING),
            () -> assertThat(changedOrder.getOrderLineItems()).flatExtracting(OrderLineItem::getSeq).containsExactlyInAnyOrderElementsOf(orderLineItemsSeqs)
        );
    }
}