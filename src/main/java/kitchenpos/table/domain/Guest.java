package kitchenpos.table.domain;

import kitchenpos.global.exception.KitchenposException;

import javax.persistence.Embeddable;

import static kitchenpos.global.exception.ExceptionInformation.ORDER_TABLE_GUEST_OUT_OF_BOUNCE;

@Embeddable
public class Guest {
    private static final int MIN_GUEST_BOUND = 0;

    private int numberOfGuests;

    protected Guest() {
    }

    private Guest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }


    public static Guest create(final int numberOfGuests) {
        validateBound(numberOfGuests);
        return new Guest(numberOfGuests);
    }

    private static void validateBound(final long numberOfGuests) {
        if (numberOfGuests < MIN_GUEST_BOUND) {
            throw new KitchenposException(ORDER_TABLE_GUEST_OUT_OF_BOUNCE);
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
}
