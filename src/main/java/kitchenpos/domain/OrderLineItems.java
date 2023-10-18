package kitchenpos.domain;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

public class OrderLineItems {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private List<OrderLineItem> values;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> values) {
        if (values.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }
        this.values = values;
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
