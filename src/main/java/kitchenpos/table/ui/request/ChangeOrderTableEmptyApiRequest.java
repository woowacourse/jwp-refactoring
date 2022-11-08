package kitchenpos.table.ui.request;

import com.fasterxml.jackson.annotation.JsonCreator;

import kitchenpos.table.application.request.ChangeOrderTableEmptyRequest;

public class ChangeOrderTableEmptyApiRequest {

    private final boolean empty;

    @JsonCreator
    public ChangeOrderTableEmptyApiRequest(boolean empty) {
        this.empty = empty;
    }

    public ChangeOrderTableEmptyRequest toServiceRequest(Long orderTableId) {
        return new ChangeOrderTableEmptyRequest(orderTableId, empty);
    }
}
