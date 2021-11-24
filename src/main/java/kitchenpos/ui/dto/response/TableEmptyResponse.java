package kitchenpos.ui.dto.response;

public class TableEmptyResponse {

    private Boolean empty;

    private TableEmptyResponse() {
    }

    public TableEmptyResponse(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
