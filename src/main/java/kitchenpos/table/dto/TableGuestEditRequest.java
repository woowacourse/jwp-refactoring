package kitchenpos.table.dto;

import javax.validation.constraints.Min;

public class TableGuestEditRequest {

    @Min(0)
    private Integer numberOfGuests;

    public TableGuestEditRequest() {
    }

    public TableGuestEditRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
