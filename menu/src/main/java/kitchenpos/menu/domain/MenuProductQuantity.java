package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
    @Column(nullable = false)
    private long quantity;

    protected MenuProductQuantity() {
    }

    public MenuProductQuantity(final long quantity) {
        validate(quantity);
        this.quantity = quantity;
    }

    private void validate(final long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
