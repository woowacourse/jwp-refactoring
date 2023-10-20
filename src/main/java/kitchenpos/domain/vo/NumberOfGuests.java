package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(int numberOfGuests) {
        validate(numberOfGuests);

        return new NumberOfGuests(numberOfGuests);
    }

    private static void validate(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
