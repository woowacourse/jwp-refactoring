package kitchenpos.table.dto.request;

import javax.validation.constraints.NotNull;

public class ChangeTableEmptyRequest {

    @NotNull
    private boolean empty;

    public ChangeTableEmptyRequest() {
    }

    public ChangeTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
