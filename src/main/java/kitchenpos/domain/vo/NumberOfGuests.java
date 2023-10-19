package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int MINIMUM_NUMBER = 0;

    @Column(name = "number_of_guests")
    private final int number;

    protected NumberOfGuests() {
        this.number = MINIMUM_NUMBER;
    }

    public NumberOfGuests(final int number) {
        validate(number);
        this.number = number;
    }

    private static void validate(final int number) {
        if (number < MINIMUM_NUMBER) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumber() {
        return number;
    }
}
