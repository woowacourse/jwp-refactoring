package kitchenpos.domain.ordertable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.exception.InvalidGuestNumberException;

@Embeddable
public class TableStatus {

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private Boolean empty;

    public TableStatus() {

    }

    public TableStatus(final Integer numberOfGuests, final Boolean empty) {
        validateCanBeEmpty(numberOfGuests, empty);
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    private void validateCanBeEmpty(final Integer numberOfGuests, final Boolean empty) {
        if (numberOfGuests > 0 && empty) {
            throw new InvalidGuestNumberException("손님 수가 1명 이상이면 빈 테이블이 될 수 없습니다.");
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNotEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    private void validateNotEmpty() {
        if (empty) {
            throw new InvalidGuestNumberException("빈 테이블에는 손님 수를 변경할 수 없습니다.");
        }
    }

    public void changeEmpty(final boolean empty) {
        validateCanBeEmpty(numberOfGuests.getValue(), empty);
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public Boolean getEmpty() {
        return empty;
    }
}
