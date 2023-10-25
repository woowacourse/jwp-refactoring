package kitchenpos.dto.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_NUMBER_OF_GUEST = 0;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUEST) {
            throw new IllegalArgumentException("손님 수는 음수가 될 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
