package kitchenpos.ui.dto.request;

public class TableRequest {

    private Long numberOfGuests;
    private Boolean empty;

    private TableRequest() {
    }

    public TableRequest(Long numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
