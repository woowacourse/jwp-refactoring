package kitchenpos.application.request;

public class ChangeTableEmptyRequest {

    private final boolean empty;

    public ChangeTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
