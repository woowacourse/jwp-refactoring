package kitchenpos.ui.dto.response;

public class TableEmptyResponse {

    private Boolean empty;

    public TableEmptyResponse(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
