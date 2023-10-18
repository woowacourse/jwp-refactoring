package kitchenpos.table.application.dto;

public class OrderTableChangeEmptyRequest {

    private boolean isEmpty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}
