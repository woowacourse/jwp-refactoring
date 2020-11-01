package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems, Orderz order) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        this.orderLineItems = orderLineItems;
        validateNotDuplicateProduct();
        updateOrder(order);
    }

    private void validateNotDuplicateProduct() {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItems.size() != new LinkedHashSet<>(menuIds).size()) {
            throw new IllegalArgumentException();
        }
    }

    public void updateOrder(Orderz order) {
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.updateOrder(order);
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
