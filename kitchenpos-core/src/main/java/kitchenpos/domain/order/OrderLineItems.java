package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Embeddable
public class OrderLineItems {

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true
    )
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderLineItem> values;

    public OrderLineItems(final List<OrderLineItem> values) {
        this.values = values;
    }

    protected OrderLineItems() {
        this(Collections.emptyList());
    }

    public List<OrderLineItem> getValues() {
        return values;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(values);
    }

    public boolean isNotSameSize(final int size) {
        return values.size() != size;
    }
}
