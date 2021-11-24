package kitchenpos.ui.dto.response;

public class TableNumberOfGuestsResponse {

    private Long numberOfGuests;

    private TableNumberOfGuestsResponse() {
    }

    public TableNumberOfGuestsResponse(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
