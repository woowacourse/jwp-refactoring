package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.util.CollectionUtils;


public class OrderLineItems {

    @MappedCollection(idColumn = "ORDER_ID", keyColumn = "ORDER_KEY")
    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        this.orderLineItems = new ArrayList<>(orderLineItems);
    }

    private static void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("하나 이상의 상품을 주문하셔야 합니다.");
        }
    }


    public List<OrderLineItem> getOrderLineItems() {
        return Collections.unmodifiableList(orderLineItems);
    }
}
