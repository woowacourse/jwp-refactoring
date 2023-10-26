package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderLineItemQuantity {
    private long quantity;

    public OrderLineItemQuantity() {
    }

    public OrderLineItemQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
