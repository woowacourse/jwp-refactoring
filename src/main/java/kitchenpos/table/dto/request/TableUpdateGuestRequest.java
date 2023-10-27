package kitchenpos.table.dto.request;

import javax.validation.constraints.Min;

public class TableUpdateGuestRequest {

    @Min(0)
    private int numberOfGuests;

    protected TableUpdateGuestRequest() {
    }

    public TableUpdateGuestRequest(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
