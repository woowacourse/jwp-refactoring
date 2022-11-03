package kitchenpos.domain;

import java.util.List;

public class OrderLineItems {
    private List<OrderLineItem> items;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> items) {
        this.items = items;
        validateItem();
    }

    private void validateItem() {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("하나 이상의 메뉴를 주문해야 한다.");
        }
    }

    public List<OrderLineItem> getItems() {
        return items;
    }
}
