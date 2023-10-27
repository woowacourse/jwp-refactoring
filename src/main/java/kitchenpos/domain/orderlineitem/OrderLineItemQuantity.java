package kitchenpos.domain.orderlineitem;

import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    private long quantity;

    public OrderLineItemQuantity() {
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
