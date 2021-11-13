package kitchenpos.domain;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import kitchenpos.exception.OrderLineItemDuplicateException;
import kitchenpos.exception.OrderLineItemsEmptyException;

public class OrderLineItems {

    private final List<OrderLineItem> orderLineItems;

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
        validateEmpty(this.orderLineItems);
        validateDuplicate(this.orderLineItems);
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new OrderLineItemsEmptyException("OrderLineItem은 최소 1개 이상이어야 합니다.");
        }
    }

    private void validateDuplicate(List<OrderLineItem> orderLineItems) {
        Set<Long> orderLineItemSet = orderLineItems.stream()
            .map(OrderLineItem::getSeq)
            .collect(toSet());

        Set<Long> menuSet = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(toSet());

        if (orderLineItemSet.size() != menuSet.size()) {
            throw new OrderLineItemDuplicateException("OrderLineItem에 중복된 Menu가 포함될 수 없습니다.");
        }
    }

    public List<OrderLineItem> toList() {
        return orderLineItems;
    }
}
