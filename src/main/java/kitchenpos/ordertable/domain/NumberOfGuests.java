package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidGuestNumberException;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests", nullable = false)
    private Integer value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final Integer value) {
        validate(value);
        this.value = value;
    }

    private void validate(final Integer value) {
        if (value < 0) {
            throw new InvalidGuestNumberException("손님 수는 0명 이상이어야 합니다.");
        }
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(final Integer value) {
        validate(value);
        this.value = value;
    }
}
