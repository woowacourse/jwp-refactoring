package kitchenpos.table.dto.request;

import javax.validation.constraints.NotNull;

public class TableCreationRequest {

    @NotNull
    private final Integer numberOfGuests;
    @NotNull
    private final Boolean empty;

    public TableCreationRequest(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
