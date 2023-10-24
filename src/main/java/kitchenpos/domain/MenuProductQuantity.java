package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductQuantity {
    @Column(nullable = false)
    private long quantity;

    public MenuProductQuantity() {
    }

    public MenuProductQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public long getQuantity() {
        return quantity;
    }
}
