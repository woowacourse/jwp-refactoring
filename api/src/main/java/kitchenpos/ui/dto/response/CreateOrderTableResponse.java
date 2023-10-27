package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.CreateOrderTableDto;

public class CreateOrderTableResponse {

    private final Long id;
    private final Long tableGroupId;
    private final int numberOfGuests;
    private final boolean empty;

    public CreateOrderTableResponse(final CreateOrderTableDto dto) {
        this.id = dto.getId();
        this.tableGroupId = dto.getTableGroupId();
        this.numberOfGuests = dto.getNumberOfGuests();
        this.empty = dto.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
