package kitchenpos.table.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kitchenpos.table.application.dto.ChangeOrderTableEmptyCommand;

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
