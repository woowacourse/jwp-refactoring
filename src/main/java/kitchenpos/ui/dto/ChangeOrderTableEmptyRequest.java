package kitchenpos.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.application.dto.ordertable.ChangeOrderTableEmptyCommand;

public class ChangeOrderTableEmptyRequest {

    @JsonProperty("empty")
    private boolean empty;

    public ChangeOrderTableEmptyRequest() {
    }

    public ChangeOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public ChangeOrderTableEmptyCommand toCommand(Long orderTableId) {
        return new ChangeOrderTableEmptyCommand(orderTableId, empty);
    }
}
