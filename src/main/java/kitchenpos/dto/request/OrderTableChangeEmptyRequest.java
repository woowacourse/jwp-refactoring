package kitchenpos.dto.request;

import java.beans.ConstructorProperties;

public class OrderTableChangeEmptyRequest {
    private final boolean empty;

    @ConstructorProperties({"empty"})
    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
