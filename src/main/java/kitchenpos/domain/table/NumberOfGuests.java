package kitchenpos.domain.table;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.InvalidNumberOfGuestsException;

@Embeddable
public class NumberOfGuests {

    @Column
    private int numberOfGuests;

    public NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests create(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new InvalidNumberOfGuestsException();
        }

        return new NumberOfGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
