package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Embeddable
public class GuestNumber {

    @Column(name = "number_of_guests")
    private int value;

    protected GuestNumber() {
    }

    public GuestNumber(final int value) {
        validatePositive(value);
        this.value = value;
    }

    private void validatePositive(final int value) {
        if (value < 0) {
            throw new DomainLogicException(CustomError.TABLE_GUEST_NUMBER_NEGATIVE_ERROR);
        }
    }

    public GuestNumber changeTo(final int value) {
        return new GuestNumber(value);
    }

    public boolean isGreaterThan(final int value) {
        return this.value > value;
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
        GuestNumber that = (GuestNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
