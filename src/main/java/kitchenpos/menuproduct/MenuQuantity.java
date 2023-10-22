package kitchenpos.menuproduct;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuQuantity {

    private long quantity;

    protected MenuQuantity() {
    }

    public MenuQuantity(final long quantity) {
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
        final MenuQuantity that = (MenuQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
