package kitchenpos.domain.order;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.Getter;

@Embeddable
@Getter
public class OrderLineItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderLineItem> value;

    protected OrderLineItems() {
    }

    public OrderLineItems(final List<OrderLineItem> value) {
        if (value == null) {
            this.value = new ArrayList<>();
            return;
        }
        validateDuplicatedMenuExists(value);
        this.value = value;
    }

    private void validateDuplicatedMenuExists(final List<OrderLineItem> value) {
        final long menuSize = value.stream()
                .map(it -> it.getMenuId())
                .distinct()
                .count();
        if (menuSize != value.size()) {
            throw new IllegalArgumentException("중복된 menu가 존재합니다.");
        }
    }
}
