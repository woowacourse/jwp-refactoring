package kitchenpos.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(name = "quantity", nullable = false)
    private Long value;

    protected Quantity() {
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
}
