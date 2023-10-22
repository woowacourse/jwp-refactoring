package kitchenpos.domain.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidQuantityException;

@Embeddable
public class Quantity {

    @Column(name = "quantity")
    private final long value;

    protected Quantity() {
        value = 0L;
    }

    public Quantity(final long value) {
        validateQuantity(value);

        this.value = value;
    }

    private void validateQuantity(final long quantity) {
        if (quantity <= 0L) {
            throw new InvalidQuantityException();
        }
    }

    public long getValue() {
        return value;
    }
}
