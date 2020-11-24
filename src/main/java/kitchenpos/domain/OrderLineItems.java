package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dto.order.OrderLineItemRequest;

public class OrderLineItems {

    private List<OrderLineItem> orderLineItems;

    private OrderLineItems(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private static void validate(List<OrderLineItem> orderLineItemRequests) {
        if (Objects.isNull(orderLineItemRequests) || orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("하나 이상의 [메뉴&수량]을 지정해야합니다.");
        }
    }

    public static OrderLineItems from(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        List<OrderLineItem> orderLineItems = orderLineItemRequests.stream()
            .map(x -> new OrderLineItem(order.getId(), x.getMenuId(), x.getQuantity()))
            .collect(Collectors.toList());

        return new OrderLineItems(orderLineItems);
    }

    public List<Long> getMenuIds() {
        List<Long> menuIds = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());

        return Collections.unmodifiableList(menuIds);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
