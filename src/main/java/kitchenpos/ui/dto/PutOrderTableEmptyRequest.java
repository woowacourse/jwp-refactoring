package kitchenpos.ui.dto;

public class PutOrderTableEmptyRequest {

    private boolean empty;

    public PutOrderTableEmptyRequest() {
    }

    public PutOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
