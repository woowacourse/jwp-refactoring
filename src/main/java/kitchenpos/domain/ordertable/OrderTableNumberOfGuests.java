package kitchenpos.domain.ordertable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableNumberOfGuests {
    private static final int MIN_VALUE = 0;
    @Column(nullable = false)
    private int numberOfGuests;

    public OrderTableNumberOfGuests() {
    }

    public OrderTableNumberOfGuests(final int numberOfGuests) {
        validation(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validation(final int numberOfGuests) {
        if (numberOfGuests < MIN_VALUE) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
