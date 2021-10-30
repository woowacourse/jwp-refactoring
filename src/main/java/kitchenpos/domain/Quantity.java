package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidArgumentException;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private long value;

    public Quantity(Long value) {
        validate(value);
        this.value = value;
    }

    protected Quantity() {
    }

    private void validate(Long value) {
        if (Objects.isNull(value) || value < 0) {
            throw new InvalidArgumentException("Quantity는 null이거나 0보다 작을 수 없습니다.");
        }
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
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
