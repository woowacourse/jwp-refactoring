package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class OrderLineItems {
    private final List<OrderLineItem> values;

    public OrderLineItems(List<OrderLineItem> values) {
        validate(values);
        this.values = values;
    }

    private void validate(List<OrderLineItem> values) {
        if (CollectionUtils.isEmpty(values)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isDifferentSize(long size) {
        return this.values.size() != size;
    }

    public void connectOrder(Order order) {
        for (OrderLineItem orderLineItem : values) {
            orderLineItem.connectOrder(order);
        }
    }

    public List<Long> getMenuIds() {
        return values.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
