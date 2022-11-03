package kitchenpos.ui.dto;

public class ChangeTableEmptyRequest {

    private Boolean empty;

    protected ChangeTableEmptyRequest() {
    }

    public ChangeTableEmptyRequest(final Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
