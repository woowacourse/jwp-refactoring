package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    List<OrderLineItem> items = new ArrayList<>();

    protected OrderLineItems() {
    }

    private OrderLineItems(final List<OrderLineItem> items) {
        this.items = items;
    }

    public static OrderLineItems from(List<OrderLineItem> orderLineItems) {
        validate(orderLineItems);
        return new OrderLineItems(orderLineItems);
    }

    private static void validate(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("최소 하나의 메뉴를 포함해야합니다.");
        }
    }

    public List<OrderLineItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
