package kitchenpos.application.dto;

public class TableChangeEmptyRequest {
    private final Boolean empty;

    public TableChangeEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
