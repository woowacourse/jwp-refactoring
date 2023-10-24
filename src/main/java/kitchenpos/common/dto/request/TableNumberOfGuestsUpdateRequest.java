package kitchenpos.common.dto.request;

import javax.validation.constraints.NotNull;

public class TableNumberOfGuestsUpdateRequest {

    @NotNull
    private final Integer numberOfGuests;

    private TableNumberOfGuestsUpdateRequest() {
        this(null);
    }

    public TableNumberOfGuestsUpdateRequest(Integer numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }
}
