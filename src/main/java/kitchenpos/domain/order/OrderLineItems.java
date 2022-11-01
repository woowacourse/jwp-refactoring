package kitchenpos.domain.order;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

public class OrderLineItems {

    private final List<OrderLineItem> value;

    public OrderLineItems(final List<OrderLineItem> value) {
        validateOrderLineItemIsNotEmpty(value);
        this.value = value;
    }

    private void validateOrderLineItemIsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }
    }

    public List<OrderLineItem> getValue() {
        return Collections.unmodifiableList(value);
    }
}
