package kitchenpos.domain.common;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validateNotNegative(value);
        this.value = value;
    }

    private static void validateNotNegative(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
