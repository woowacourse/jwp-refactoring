package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    public static final int ZERO = 0;

    @Column(name = "number_of_guests")
    private int value;

    public NumberOfGuests(final int value) {
        validate(value);
        this.value = value;
    }

    protected NumberOfGuests() {
    }

    private void validate(final int value) {
        if (value < ZERO) {
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
        if (!(o instanceof NumberOfGuests)) {
            return false;
        }
        final NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
