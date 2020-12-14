package kitchenpos.table.dto;

import java.beans.ConstructorProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import kitchenpos.table.domain.OrderTable;

public class TableCreateRequest {
    @NotNull
    @Min(value = 0, message = "0 이상 값만 가능합니다.")
    private final Integer numberOfGuests;
    @NotNull
    private final Boolean empty;

    @ConstructorProperties({"numberOfGuests", "empty"})
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
