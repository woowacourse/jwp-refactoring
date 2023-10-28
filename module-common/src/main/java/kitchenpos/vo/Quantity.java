package kitchenpos.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

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
            throw new IllegalArgumentException("수량은 음수가 될 수 없습니다.");
        }
    }

    public long value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity quantity = (Quantity) o;
        return value == quantity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
