package kitchenpos.domain.order;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderLineItem> value;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> value, final Order order) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 존재하지 않습니다.");
        }
        validateDuplicatedMenuExists(value);
        this.value = value;
        for (OrderLineItem orderLineItem : value) {
            orderLineItem.changeOrder(order);
        }
    }

    private void validateDuplicatedMenuExists(final List<OrderLineItem> value) {
        final long menuSize = value.stream()
                .map(it -> it.getMenuId())
                .distinct()
                .count();
        if (menuSize != value.size()) {
            throw new IllegalArgumentException("중복된 menu가 존재합니다.");
        }
    }

    public List<OrderLineItem> getValue() {
        return value;
    }
}
