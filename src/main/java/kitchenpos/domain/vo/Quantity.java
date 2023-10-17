package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    @Column(nullable = false)
    private long quantity;

    protected Quantity() {
    }

    public Quantity(long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("최소 1개의 수량은 필요합니다.");
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
