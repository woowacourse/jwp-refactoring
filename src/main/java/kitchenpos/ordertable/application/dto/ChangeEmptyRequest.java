package kitchenpos.ordertable.application.dto;

public class ChangeEmptyRequest {

    private final boolean empty;

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
