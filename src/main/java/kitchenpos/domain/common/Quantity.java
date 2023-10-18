package kitchenpos.domain.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidQuantityException;

@Embeddable
public class Quantity {

    @Column
    private final long quantity;

    protected Quantity() {
        quantity = 0L;
    }

    public Quantity(final long quantity) {
        validateQuantity(quantity);

        this.quantity = quantity;
    }

    private void validateQuantity(final long quantity) {
        if (quantity <= 0L) {
            throw new InvalidQuantityException();
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
