package kitchenpos.order.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> orderLineItems) {
        validateOrderLineItemsNotEmpty(orderLineItems);
        validateMenuNotDuplicated(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    private void validateOrderLineItemsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }
    }

    private void validateMenuNotDuplicated(final List<OrderLineItem> orderLineItems) {
        if (isMenuDuplicated(orderLineItems)) {
            throw new IllegalArgumentException("중복된 메뉴의 주문 항목이 존재합니다.");
        }
    }

    private boolean isMenuDuplicated(final List<OrderLineItem> orderLineItems) {
        final var itemCount = orderLineItems.size();
        final var menuCount = countMenuDistinct(orderLineItems);
        return itemCount != menuCount;
    }

    private static long countMenuDistinct(final List<OrderLineItem> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItem::getMenuHistoryId)
                .distinct()
                .count();
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
