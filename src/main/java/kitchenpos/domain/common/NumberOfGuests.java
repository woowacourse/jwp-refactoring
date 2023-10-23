package kitchenpos.domain.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidNumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests")
    private final int value;

    public NumberOfGuests() {
        this.value = 0;
    }

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
