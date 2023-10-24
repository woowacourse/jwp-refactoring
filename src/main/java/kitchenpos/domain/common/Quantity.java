package kitchenpos.domain.common;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class Quantity {
    public static final String PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE = "수량은 0보다 커야 합니다.";
    @NotNull
    private long quantity;

    protected Quantity() {
    }

    private Quantity(final long quantity) {
        validateQuantity(quantity);
        this.quantity = quantity;
    }

    private static void validateQuantity(final long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(PRODUCT_QUANTITY_IS_UNDER_ONE_ERROR_MESSAGE);
        }
    }

    public static Quantity of(final long quantity) {
        return new Quantity(quantity);
    }

    public long getQuantity() {
        return quantity;
    }
}
