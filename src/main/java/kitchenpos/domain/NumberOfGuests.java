package kitchenpos.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;

@Embeddable
@Access(value = AccessType.FIELD)
public class NumberOfGuests {

    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests from(final int numberOfGuests) {
        validate(numberOfGuests);
        return new NumberOfGuests(numberOfGuests);
    }

    private static void validate(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 최소 한명이어야합니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
