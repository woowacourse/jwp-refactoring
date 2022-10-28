package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.EmptyDataException;
import kitchenpos.exception.InvalidGuestNumberException;

@Embeddable
public class GuestNumber {

    private static final int GUEST_NUMBER_MIN_VALUE = 1;

    @Column(name = "number_of_guests")
    private int value;

    protected GuestNumber() {
    }

    private GuestNumber(int value) {
        this.value = value;
    }

    public static GuestNumber from(Integer value) {
        validate(value);
        return new GuestNumber(value);
    }

    private static void validate(Integer value) {
        if (Objects.isNull(value)) {
            throw new EmptyDataException(GuestNumber.class.getSimpleName());
        }
        if (value < GUEST_NUMBER_MIN_VALUE) {
            throw new InvalidGuestNumberException();
        }
    }

    public int getValue() {
        return value;
    }
}
