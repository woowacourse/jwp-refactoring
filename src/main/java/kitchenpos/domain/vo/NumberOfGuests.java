package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int MINIMUM_GUEST = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < MINIMUM_GUEST) {
            throw new IllegalArgumentException("테이블 인원 수가 0보다 작을 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
