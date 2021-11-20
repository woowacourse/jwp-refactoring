package kitchenpos.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "orderId")
    private List<OrderLineItem> orderLineItems;

    protected OrderLineItems() {

    }

    public OrderLineItems(OrderLineItem... orderLineItems) {
        this(Arrays.asList(orderLineItems));
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        this.orderLineItems = orderLineItems;
    }

    public void updateOrderId(Order order) {
        validateNotEmpty();
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrderInfo(order);
        }
    }

    public void validateNotEmpty() {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public void validateSameSize(long size) {
        if (orderLineItems.size() != size) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> toList() {
        return Collections.unmodifiableList(orderLineItems);
    }

    public List<Long> getMenuIds() {
        return orderLineItems.stream()
            .map(OrderLineItem::getMenuId)
            .collect(Collectors.toList());
    }
}
