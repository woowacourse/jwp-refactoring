package kitchenpos.ordertable.application.dto;

public class TableChangeEmptyStatusRequest {

    private final Boolean empty;

    private TableChangeEmptyStatusRequest() {
        this(null);
    }

    public TableChangeEmptyStatusRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
