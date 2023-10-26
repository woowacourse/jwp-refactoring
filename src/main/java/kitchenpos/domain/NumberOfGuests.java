package kitchenpos.domain;

import static kitchenpos.exception.NumberOfGuestsExceptionType.NEGATIVE_VALUE_EXCEPTION;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.NumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests")
    private final int value;

    @Deprecated
    protected NumberOfGuests() {
        value = 0;
    }

    public NumberOfGuests(int value) {
        checkNegative(value);
        this.value = value;
    }

    private void checkNegative(int value) {
        if (value < 0) {
            throw new NumberOfGuestsException(NEGATIVE_VALUE_EXCEPTION);
        }
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
