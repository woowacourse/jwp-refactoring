package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItems(orderLineItems);

        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItems(final List<OrderLineItem> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 비어있습니다.");
        }

        validateDuplicateMenu(orderLineItems);
    }

    private void validateDuplicateMenu(final List<OrderLineItem> orderLineItems) {
        final long menuCount = orderLineItems.stream()
                                             .map(OrderLineItem::getMenuId)
                                             .distinct()
                                             .count();

        if (menuCount != orderLineItems.size()) {
            throw new IllegalArgumentException("주문 항목의 메뉴는 중복될 수 없습니다.");
        }
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    public Set<Long> getMenuIds() {
        return orderLineItems.stream()
                             .map(OrderLineItem::getMenuId)
                             .collect(Collectors.toSet());
    }
}
