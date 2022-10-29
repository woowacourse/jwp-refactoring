package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests", nullable = false)
    private final int count;

    protected NumberOfGuests() {
        this(0);
    }

    public NumberOfGuests(int count) {
        validatePositive(count);
        this.count = count;
    }

    private void validatePositive(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("손님의 수는 음수가 될 수 없습니다 : " + count);
        }
    }

    public int getCount() {
        return count;
    }
}
