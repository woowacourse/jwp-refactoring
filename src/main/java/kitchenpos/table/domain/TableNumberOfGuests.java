package kitchenpos.table.domain;

import javax.persistence.Embeddable;

@Embeddable
public class TableNumberOfGuests {

    private Integer numberOfGuests;

    public TableNumberOfGuests() {
    }

    public TableNumberOfGuests(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeGuest(int numberOfGuests, boolean isEmpty) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }

        if (isEmpty) {
            throw new IllegalArgumentException();
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
