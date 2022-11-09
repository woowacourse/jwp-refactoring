package kitchenpos.order.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> value;

    public OrderLineItems(final List<OrderLineItem> value) {
        validateEmpty(value);
        this.value = value;
    }

    private void validateEmpty(final List<OrderLineItem> value) {
        if (CollectionUtils.isEmpty(value)) {
            throw new IllegalArgumentException();
        }
    }

    public int getSize() {
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
