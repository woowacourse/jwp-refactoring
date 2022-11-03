package kitchenpos.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderLineItems {
    private final List<OrderLineItem> items;
    public OrderLineItems(List<OrderLineItem> items) {
        this.items = items;
        validateItem();
    }

    private void validateItem() {
        if (CollectionUtils.isEmpty(items)) {
            throw new IllegalArgumentException("하나 이상의 메뉴를 주문해야 한다.");
        }
    }

    public List<OrderLineItem> getItems() {
        return items;
    }
}
