package kitchenpos.domain;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private List<OrderLineItem> value;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        checkEmpty(Collections.unmodifiableList(orderLineItems));
        this.value = orderLineItems;
    }

    private void checkEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public long size() {
        return value.size();
    }

    public List<Long> getMenuIds() {
        return value.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());
    }

    public List<OrderLineItem> getValue() {
        return value;
    }
}
