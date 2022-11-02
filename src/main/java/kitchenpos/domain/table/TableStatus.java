package kitchenpos.domain.table;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import kitchenpos.exception.CustomError;
import kitchenpos.exception.DomainLogicException;

@Embeddable
public class TableStatus {

    @Embedded
    private Empty empty;

    @Embedded
    private GuestNumber numberOfGuests;

    protected TableStatus() {
    }

    public TableStatus(final Empty empty, final GuestNumber numberOfGuests) {
        validate(empty, numberOfGuests);
        this.empty = empty;
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(final Empty empty, final GuestNumber numberOfGuests) {
        if (numberOfGuests.isGreaterThan(0) && empty.isEmpty()) {
            throw new DomainLogicException(CustomError.TABLE_STATUS_INVALID_ERROR);
        }
    }

    public void changeEmpty(final boolean empty) {
        this.empty = this.empty.changeTo(empty);
        resetGuestNumber(empty);
    }

    private void resetGuestNumber(boolean empty) {
        if (empty) {
            this.numberOfGuests = this.numberOfGuests.changeTo(0);
        }
    }

    public void changeGuestNumber(final int number) {
        validateNotEmpty();
        this.numberOfGuests = this.numberOfGuests.changeTo(number);
    }

    private void validateNotEmpty() {
        if (empty.isEmpty()) {
            throw new DomainLogicException(CustomError.TABLE_EMPTY_BUT_CHANGE_GUEST_NUMBER_ERROR);
        }
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getValue();
    }
}
