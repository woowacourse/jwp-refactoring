package kitchenpos.domain.order.service.dto;

public class OrderTableUpdateRequest {

    private final boolean empty;

    public OrderTableUpdateRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
