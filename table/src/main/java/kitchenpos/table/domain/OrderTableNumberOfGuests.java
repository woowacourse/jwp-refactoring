package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableNumberOfGuests {
    private static final int MIN_VALUE = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected OrderTableNumberOfGuests() {
    }

    public OrderTableNumberOfGuests(final int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(final int numberOfGuests) {
        if (numberOfGuests < MIN_VALUE) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
