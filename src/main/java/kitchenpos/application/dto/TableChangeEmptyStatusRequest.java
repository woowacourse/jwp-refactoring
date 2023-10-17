package kitchenpos.application.dto;

public class TableChangeEmptyStatusRequest {

    private final Boolean empty;

    public TableChangeEmptyStatusRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
