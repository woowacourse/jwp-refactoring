package kitchenpos.domain;

public class Price {

    private final long value;

    public Price(long value) {
        validate(value);
        this.value = value;
    }

    private void validate(long value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isExpensiveThan(Price other) {
        return value > other.getValue();
    }

    public Price add(long additionalPrice) {
        return new Price(value + additionalPrice);
    }

    public long getValue() {
        return value;
    }
}
