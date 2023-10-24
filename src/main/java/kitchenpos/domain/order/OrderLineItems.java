package kitchenpos.domain.order;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

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
                                             .map(OrderLineItem::getMenu)
                                             .distinct()
                                             .count();

        if (menuCount != orderLineItems.size()) {
            throw new IllegalArgumentException("주문 항목의 메뉴는 중복될 수 없습니다.");
        }
    }

    public void add(final OrderLineItem orderLineItem) {
        orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
