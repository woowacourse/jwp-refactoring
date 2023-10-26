package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class NumberOfGuests {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @NotNull
    private Integer numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(Integer numberOfGuests) {
        validate(numberOfGuests);

        this.numberOfGuests = numberOfGuests;
    }

    private void validate(Integer numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("테이블에 방문한 손님 수는 0 이상이어야 합니다.");
        }
    }

    public static NumberOfGuests from(Integer numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

}
