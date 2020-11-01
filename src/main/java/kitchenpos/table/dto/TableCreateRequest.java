package kitchenpos.table.dto;

import javax.validation.constraints.NotNull;

import kitchenpos.table.domain.Table;

public class TableCreateRequest {

    @NotNull
    private Integer numberOfGuests;

    @NotNull
    private Boolean empty;

    public TableCreateRequest() {
    }

    public TableCreateRequest(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public Table toEntity() {
        return new Table(null, numberOfGuests, empty);
    }
}
