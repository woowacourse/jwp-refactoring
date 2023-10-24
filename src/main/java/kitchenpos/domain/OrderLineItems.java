package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "order")
    private List<OrderLineItem> orderLineItems;

    public OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> orderLineItems) {
        validateEmpty(orderLineItems);
        this.orderLineItems = orderLineItems;
    }

    public void changeOrder(Order order){
        for (OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.changeOrder(order);
        }
    }

    private void validateEmpty(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public boolean hasSameSizeWith(Long size) {
        return orderLineItems.size() == size;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }
}
