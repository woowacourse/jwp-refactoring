package kitchenpos.order.domain;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import kitchenpos.order.exception.OrderLineItemDuplicateException;
import kitchenpos.order.exception.OrderLineItemsEmptyException;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

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
        Set<Long> menuSet = orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(toSet());

        if (orderLineItems.size() != menuSet.size()) {
            throw new OrderLineItemDuplicateException("OrderLineItem에 중복된 Menu가 포함될 수 없습니다.");
        }
    }

    public List<OrderLineItem> toList() {
        return orderLineItems;
    }
}
