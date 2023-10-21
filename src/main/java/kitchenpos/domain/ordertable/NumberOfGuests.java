package kitchenpos.domain.ordertable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidGuestNumberException;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests")
    private int value;

    public NumberOfGuests() {
    }

    public NumberOfGuests(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new InvalidGuestNumberException("손님 수는 0명 이상이어야 합니다.");
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(final int value) {
        validate(value);
        this.value = value;
    }
}
