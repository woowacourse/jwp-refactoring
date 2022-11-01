package kitchenpos.domain.common;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long value;

    protected Quantity() {
    }

    public Quantity(final long value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final long value) {
        if (value <= 0) {
            throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
        }
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quantity)) {
            return false;
        }
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
