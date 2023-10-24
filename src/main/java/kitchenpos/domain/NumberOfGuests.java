package kitchenpos.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        if (value < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 음수일 수 없습니다.");
        }

        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
