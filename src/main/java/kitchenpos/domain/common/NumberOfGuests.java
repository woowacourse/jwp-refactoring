package kitchenpos.domain.common;

import kitchenpos.domain.exception.InvalidNumberOfGuestsException;

public class NumberOfGuests {

    private final int value;

    public NumberOfGuests(final int value) {
        validateValue(value);

        this.value = value;
    }

    private void validateValue(final int value) {
        if (value < 0) {
            throw new InvalidNumberOfGuestsException();
        }
    }

    public int getValue() {
        return value;
    }
}
