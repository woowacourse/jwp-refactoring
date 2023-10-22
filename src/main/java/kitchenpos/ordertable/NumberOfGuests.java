package kitchenpos.ordertable;

import javax.persistence.Column;

public class NumberOfGuests {

    @Column(name = "number_of_guests")
    private int value;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("테이블 인원은 양수여야합니다.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
