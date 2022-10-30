package kitchenpos.domain;

public class Quantity {

    private final Long value;

    public Quantity(final Long value) {
        validatePositiveQuantity(value);
        this.value = value;
    }

    private void validatePositiveQuantity(final Long value) {
        if (value < 1) {
            throw new IllegalArgumentException();
        }
    }

    public Long getValue() {
        return value;
    }
}
