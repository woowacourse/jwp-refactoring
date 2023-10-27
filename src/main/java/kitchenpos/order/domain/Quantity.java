package kitchenpos.order.domain;

import java.util.Objects;

public class Quantity {

    private final long value;

    public Quantity(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
        this.value = value;
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
