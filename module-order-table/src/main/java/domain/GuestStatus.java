package domain;

import exception.TableException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GuestStatus {

    public static final GuestStatus EMPTY_GUEST_STATUS = new GuestStatus();

    private final int numberOfGuests;
    @Column(name = "empty")
    private final boolean isEmpty;

    protected GuestStatus() {
        this.numberOfGuests = 0;
        isEmpty = false;
    }

    public GuestStatus(final int numberOfGuests, final boolean isEmpty) {
        this.numberOfGuests = numberOfGuests;
        this.isEmpty = isEmpty;
    }

    public GuestStatus changeEmpty(final boolean isEmpty) {
        return new GuestStatus(this.numberOfGuests, isEmpty);
    }

    public GuestStatus changeNumberOfGuest(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateNotEmpty();
        return new GuestStatus(numberOfGuests, this.isEmpty);
    }

    private void validateNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new TableException.NoGuestException();
        }
    }

    private void validateNotEmpty() {
        if (isEmpty) {
            throw new TableException.TableEmptyException();
        }
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
