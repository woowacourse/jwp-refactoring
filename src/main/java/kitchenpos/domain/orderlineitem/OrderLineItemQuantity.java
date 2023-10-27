package kitchenpos.domain.orderlineitem;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    @Column(nullable = false)
    private long quantity;

    protected OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(final long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
