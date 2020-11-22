package kitchenpos.domain.table;

import kitchenpos.exception.InvalidNumberOfGuestsException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Column(name = "numberOfGuests")
    private int value;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int value) {
        this.value = value;
    }

    public static NumberOfGuests from(int value) {
        validateValue(value);

        return new NumberOfGuests(value);
    }

    private static void validateValue(int value) {
        if (value < MIN_NUMBER_OF_GUESTS) {
            throw new InvalidNumberOfGuestsException("방문 손님 수는 " + MIN_NUMBER_OF_GUESTS + "명 이상이어야 합니다!");
        }
    }

    public int getValue() {
        return value;
    }
}
