package kitchenpos.order.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItemGroup {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<OrderLineItem> orderLineItems = new ArrayList<>();

    public static OrderLineItemGroup from(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        final OrderLineItemGroup orderLineItemGroup = new OrderLineItemGroup();
        orderLineItemGroup.orderLineItems = orderLineItems;
        return orderLineItemGroup;
    }

    public static OrderLineItemGroup from(List<OrderLineItem> orderLineItems, Order order) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(order);
        }
        final OrderLineItemGroup orderLineItemGroup = new OrderLineItemGroup();
        orderLineItemGroup.orderLineItems = orderLineItems;
        return orderLineItemGroup;
    }

    public List<OrderLineItem> value() {
        return orderLineItems;
    }
}
