package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST)
    private final List<OrderLineItem> orderLineItems = new ArrayList<>();

    public OrderLineItems() {
    }

    public static OrderLineItems from(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("OrderLineItems는 비어있을 수 없습니다.");
        }

        final OrderLineItems result = new OrderLineItems();
        orderLineItems.forEach(result::add);
        return result;
    }

    public void add(final OrderLineItem orderLineItem) {
        this.orderLineItems.add(orderLineItem);
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
