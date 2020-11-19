package kitchenpos.dto;

public class TableChangeRequest {

    private int numberOfGuests;
    private boolean empty;

    private TableChangeRequest() {
    }

    private TableChangeRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableChangeRequest(int numberOfGuests) {
        this(numberOfGuests, false);
    }

    public TableChangeRequest(boolean empty) {
        this(0, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
