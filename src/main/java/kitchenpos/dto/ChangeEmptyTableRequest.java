package kitchenpos.dto;

public class ChangeEmptyTableRequest {

    private Boolean empty;

    public ChangeEmptyTableRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
