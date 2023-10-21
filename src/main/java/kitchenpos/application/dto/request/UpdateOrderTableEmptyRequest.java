package kitchenpos.application.dto.request;

public class UpdateOrderTableEmptyRequest {

    private Boolean empty;

    public UpdateOrderTableEmptyRequest() {
    }

    public UpdateOrderTableEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
