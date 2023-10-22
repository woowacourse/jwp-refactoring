package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Quantity {
    private long quantity;

    protected Quantity() {
    }

    public Quantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Quantity quantity = (Quantity) o;
        return this.quantity == quantity.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
