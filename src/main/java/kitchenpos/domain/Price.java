package kitchenpos.domain;

public class Price {

    private long value;

    public Price(long value) {
        validate(value);
        this.value = value;
    }

    private void validate(long value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public long getValue() {
        return value;
    }
}
