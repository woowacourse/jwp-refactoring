package kitchenpos.ui.dto.response;

import kitchenpos.application.dto.ChangeEmptyOrderTableDto;

public class UpdateEmptyOrderTableResponse {

    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public UpdateEmptyOrderTableResponse(final ChangeEmptyOrderTableDto dto) {
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
