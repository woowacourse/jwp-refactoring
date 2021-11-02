package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity")
    private long value;

    protected Quantity() {
    }

    public Quantity(final long value) {
        validateValue(value);
        this.value = value;
    }

    private void validateValue(final long value) {
        if (value < 0) {
            throw new IllegalArgumentException("수량은 0 이상입니다.");
        }
    }

    public long getValue() {
        return value;
    }
}
