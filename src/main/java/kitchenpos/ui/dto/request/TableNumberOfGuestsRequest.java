package kitchenpos.ui.dto.request;

public class TableNumberOfGuestsRequest {

    private Long numberOfGuests;

    public TableNumberOfGuestsRequest(Long numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getNumberOfGuests() {
        return numberOfGuests;
    }
}
