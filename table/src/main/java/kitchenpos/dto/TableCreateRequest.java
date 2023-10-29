package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class TableCreateRequest {

    @NotNull
    private final Integer numberOfGuests;

    @NotNull
    private final Boolean empty;

    public TableCreateRequest(final Integer numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean isEmpty() {
        return empty;
    }
}
