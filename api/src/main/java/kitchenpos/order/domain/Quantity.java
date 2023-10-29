package kitchenpos.order.domain;

import java.util.Objects;

public class Quantity {

    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 0보다 작을 수 없습니다.");
        }
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
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
        return this.quantity == quantity.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}
