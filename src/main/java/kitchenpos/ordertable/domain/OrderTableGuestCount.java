package kitchenpos.ordertable.domain;

public class OrderTableGuestCount {

    private static final int MINIMUM_GUEST_COUNT = 0;

    private final int count;

    public OrderTableGuestCount(final int count) {
        validateCount(count);
        this.count = count;
    }

    private int validateCount(final int count) {
        if (count < MINIMUM_GUEST_COUNT) {
            throw new IllegalArgumentException();
        }
        return count;
    }

    public int getCount() {
        return count;
    }
}
