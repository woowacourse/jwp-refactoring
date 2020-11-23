package kitchenpos.dto.table;

public class TableChangeRequest {

    private int numberOfGuests;
    private boolean empty;

    public TableChangeRequest() {
    }

    private TableChangeRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public TableChangeRequest(int numberOfGuests) {
        this(numberOfGuests, false); // empty는 무의미한 데이터
    }

    public TableChangeRequest(boolean empty) {
        this(0, empty); // numberOfGuests는 무의미한 데이터
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
