package kitchenpos.domain.order;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> value;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> value) {
        validateOrderLineItemIsNotEmpty(value);
        this.value = value;
    }

    private void validateOrderLineItemIsNotEmpty(final List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }
    }

    public List<OrderLineItem> getValue() {
        return Collections.unmodifiableList(value);
    }
}
