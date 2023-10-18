package kitchenpos.domain;

import kitchenpos.exception.QuantityEmptyException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Quantity {

    private static final long EMPTY_QUANTITY = 0L;
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    protected Quantity() {
    }

    private Quantity(final Long quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(final Long quantity) {
        validateQuantityEmpty(quantity);
        return new Quantity(quantity);
    }

    private static void validateQuantityEmpty(final Long quantity) {
        if (quantity == null || quantity < EMPTY_QUANTITY) {
            throw new QuantityEmptyException();
        }
    }

    public Long getQuantity() {
        return quantity;
    }
}
