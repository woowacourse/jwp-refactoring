package kitchenpos.domain;

import static kitchenpos.exception.QuantityExceptionType.VALUE_NEGATIVE_EXCEPTION;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.QuantityException;

@Embeddable
public class Quantity {

    @Column(name = "quantity")
    private final long value;

    @Deprecated
    protected Quantity() {
        this.value = 0L;
    }

    public Quantity(long value) {
        checkNegative(value);
        this.value = value;
    }

    private void checkNegative(long value) {
        if (value < 0) {
            throw new QuantityException(VALUE_NEGATIVE_EXCEPTION);
        }
    }

    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity1 = (Quantity) o;
        return value == quantity1.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
