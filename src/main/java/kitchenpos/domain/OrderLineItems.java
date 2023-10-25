package kitchenpos.domain;

import org.springframework.util.CollectionUtils;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

public class OrderLineItems {

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<OrderLineItem> values;

    public OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> values) {
        validateEmpty(values);
        this.values = values;
    }

    private void validateEmpty(final List<OrderLineItem> values) {
        if (CollectionUtils.isEmpty(values)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderLineItem> getValues() {
        return values;
    }
}
