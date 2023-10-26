package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderTableNumberOfGuests {
    private int numberOfGuests;

    public OrderTableNumberOfGuests() {
    }

    public OrderTableNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
