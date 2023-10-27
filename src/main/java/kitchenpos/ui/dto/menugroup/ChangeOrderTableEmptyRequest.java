package kitchenpos.ui.dto.menugroup;

public class ChangeOrderTableEmptyRequest {

    private final boolean empty;

    public ChangeOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
