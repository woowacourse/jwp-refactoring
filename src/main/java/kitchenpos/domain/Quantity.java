package kitchenpos.domain;

public class Quantity {

    private static final int MINIMUM_QUANTITY = 0;

    private final long value;

    private Quantity(long value) {
        this.value = value;
    }

    public static Quantity valueOf(long value) {
        validateQuantity(value);
        return new Quantity(value);
    }

    private static void validateQuantity(long value) {
        if (value < MINIMUM_QUANTITY) {
            throw new IllegalArgumentException("개수가 " + MINIMUM_QUANTITY + " 미만일 수 없습니다.");
        }
    }

    public long getValue() {
        return value;
    }
}
