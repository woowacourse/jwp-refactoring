package kitchenpos.domain.menuproduct;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class MenuProductQuantity {
    @Column(nullable = false)
    private long quantity;

    public MenuProductQuantity() {
    }

    public MenuProductQuantity(final long quantity) {
        validation(quantity);
        this.quantity = quantity;
    }

    private void validation(final long quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException();
        }
    }

    public long getQuantity() {
        return quantity;
    }
}
