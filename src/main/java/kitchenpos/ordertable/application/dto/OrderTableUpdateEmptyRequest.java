package kitchenpos.ordertable.application.dto;

import javax.validation.constraints.NotNull;

public class OrderTableUpdateEmptyRequest {

    @NotNull
    private boolean empty;
    public OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }

}
