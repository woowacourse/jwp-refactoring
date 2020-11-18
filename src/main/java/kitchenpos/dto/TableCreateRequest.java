package kitchenpos.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import kitchenpos.domain.OrderTable;

public class TableCreateRequest {
    @NotNull
    @Min(value = 1, message = "1 이상 값만 가능합니다.")
    private final Integer numberOfGuests;
    @NotNull
    private final Boolean empty;

    public TableCreateRequest(final int numberOfGuests, final Boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable toEntity() {
        return new OrderTable(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }
}
