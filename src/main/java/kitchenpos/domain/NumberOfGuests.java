package kitchenpos.domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Access(value = AccessType.FIELD)
public class NumberOfGuests {

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(final int value) {
        this.value = value;
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

    public int getValue() {
        return value;
    }
}
