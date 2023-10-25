package kitchenpos.dto.ordertable;

public class ChangeEmptyRequest {

    private final boolean empty;

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
