package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.EmptyDataException;
import kitchenpos.order.exception.InvalidGuestNumberException;

@Embeddable
public class GuestNumber {

    private static final int GUEST_NUMBER_MIN_VALUE = 1;

    @Column(name = "number_of_guests")
    private int value;

    protected GuestNumber() {
    }

    public GuestNumber(int value) {
        validate(value);
        this.value = value;
    }

    private void validate(Integer value) {
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
