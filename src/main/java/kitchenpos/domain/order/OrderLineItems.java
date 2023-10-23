package kitchenpos.domain.order;

import static javax.persistence.CascadeType.PERSIST;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;

@Embeddable
public class OrderLineItems {

    @OneToMany(fetch = FetchType.EAGER, cascade = PERSIST)
    @JoinColumn(name = "order_id", nullable = false, updatable = false)
    private List<OrderLineItem> items;

    protected OrderLineItems() {
    }

    public OrderLineItems(List<OrderLineItem> items) {
        validate(items);
        this.items = items;
    }

    private void validate(List<OrderLineItem> items) {
        if (CollectionUtils.isEmpty(items)) {
            throw new IllegalArgumentException("주문 항목 목록이 있어야 합니다.");
        }
    }

    public List<OrderLineItem> getItems() {
        return items;
    }
}
