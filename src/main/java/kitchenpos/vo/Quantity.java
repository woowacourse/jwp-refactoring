package kitchenpos.vo;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private Long value;

    protected Quantity() {
        value = null;
    }

    public Quantity(final Long value) {
        validateQuantity(value);
        this.value = value;
    }

    private void validateQuantity(final Long value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 음수일 수 없습니다.");
        }
    }

    public Long getValue() {
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
        return Objects.equals(value, quantity.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
