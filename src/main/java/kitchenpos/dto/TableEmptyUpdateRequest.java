package kitchenpos.dto;

public class TableEmptyUpdateRequest {

    private final Boolean empty;

    public TableEmptyUpdateRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
