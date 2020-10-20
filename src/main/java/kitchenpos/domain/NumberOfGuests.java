package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(final int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public NumberOfGuests compare(final NumberOfGuests numberOfGuests) {
        if (this.numberOfGuests == numberOfGuests.getNumberOfGuests()) {
            return this;
        }
        return numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
