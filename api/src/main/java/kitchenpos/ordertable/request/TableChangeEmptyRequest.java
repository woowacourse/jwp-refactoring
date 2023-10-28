package kitchenpos.ordertable.request;

import javax.validation.constraints.NotNull;

public class TableChangeEmptyRequest {

    @NotNull
    private boolean empty;

    private TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
