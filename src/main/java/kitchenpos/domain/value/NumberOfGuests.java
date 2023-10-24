package kitchenpos.domain.value;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidNumberException;

@Embeddable
public class NumberOfGuests {

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    protected NumberOfGuests() {
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void validate(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberException("손님 수는 음수가 될 수 없습니다.");
        }
    }
}
