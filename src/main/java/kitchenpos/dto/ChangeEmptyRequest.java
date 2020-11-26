package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class ChangeEmptyRequest {

    @NotNull
    boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
