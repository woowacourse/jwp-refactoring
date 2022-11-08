package kitchenpos.domain.order;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateSize(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateSize(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.size() < 1) {
            throw new IllegalArgumentException();
        }
    }

    public OrderLineItems changeOrderId(Long orderId) {
        List<OrderLineItem> changedOrderLineItems = this.orderLineItems.stream()
                .map(each -> each.changeOrderId(orderId))
                .collect(Collectors.toUnmodifiableList());
        return new OrderLineItems(changedOrderLineItems);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
