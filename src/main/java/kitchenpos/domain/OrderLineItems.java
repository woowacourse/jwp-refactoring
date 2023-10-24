package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class OrderLineItems {

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
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

    public List<Long> getMenuIds() {
        return values.stream()
                .map(value -> value.getMenu().getId())
                .collect(Collectors.toList());
    }

    public boolean isNotSameSize(final int size) {
        return values.size() != size;
    }

    public void updateOrder(final Order order) {
        values.forEach(value -> value.setOrder(order));
    }
}
