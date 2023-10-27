package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ChangeNumberOfGuestsOrderTableDto;

public class UpdateNumberOfGuestsResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public UpdateNumberOfGuestsResponse(final ChangeNumberOfGuestsOrderTableDto dto) {
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
